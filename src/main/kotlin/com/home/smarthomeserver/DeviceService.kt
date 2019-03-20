package com.home.smarthomeserver

import com.amazonaws.services.iot.client.AWSIotConnectionStatus
import com.amazonaws.services.iot.client.AWSIotDevice
import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.smarthomeserver.awsconfig.AwsIotClient
import com.home.smarthomeserver.devices.RPILight
import com.home.smarthomeserver.entity.DeviceEntity
import com.home.smarthomeserver.entity.Status
import com.home.smarthomeserver.models.Command
import kotlinx.coroutines.future.await
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import software.amazon.awssdk.services.iot.model.CreateThingRequest
import software.amazon.awssdk.services.iot.model.DeleteThingRequest

@Service
@Transactional
class DeviceService {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var broker: AWSIotMqttClient

    @Autowired
    lateinit var deviceRepository: DeviceRepository

    fun updateDeviceStatus(id: String, deviceName: String, command: Command) {
        val device = RPILight(deviceName)
        connect(device)
        device.command = command.toString()
        device.status = Status.CONNECTED.toString()
        val json = buildJson(mapOf("command" to "$command"))
        device.update(json)
    }

    suspend fun addDevice(username: String, device: String): Boolean {
        val newThingName = device + userService.userRepository.findUserByUsername(username).id
        val thingResponse = AwsIotClient.get().createThing(CreateThingRequest.builder().run {
            thingName(newThingName)
            build()
        }).await()
        if (thingResponse.sdkHttpResponse().isSuccessful) {
            val user = userService.getUserEntity(username)
            val deviceEntity = DeviceEntity(name = device, commands = mutableListOf(), id = 1L, owner = user, thingName = newThingName)
            user.devices.add(deviceEntity)
            userService.userRepository.save(user)
        }
        return thingResponse.sdkHttpResponse().isSuccessful
    }


    suspend fun removeDevice(username: String, deviceName: String): Boolean {
        if (deviceRepository.deleteDeviceEntityByNameAndOwnerUsername(deviceName, username) > 0) {
            return AwsIotClient.get().deleteThing(
                    DeleteThingRequest.builder().thingName(deviceName).build()
            ).await().sdkHttpResponse().isSuccessful
        }
        return false
    }

    fun addFavorite(username: String, deviceName: String) {
        deviceRepository.apply {
            val prevFavorite = findDeviceEntityByNameAndOwnerUsername(username, deviceName)
            val newFavorite = findDeviceEntityByNameAndOwnerUsername(deviceName, username) ?: throw Exception()
            if (prevFavorite == null) {
                newFavorite.isFavorite = true
                save(newFavorite)
            } else {
                prevFavorite.isFavorite = false
                newFavorite.isFavorite = true
                saveAll(listOf(prevFavorite, newFavorite))
            }
        }
    }

    fun removeFavorite(username: String, deviceName: String): String {
        deviceRepository.apply {
            val prevFavorite = findDeviceEntityByNameAndOwnerUsername(deviceName, username)
            prevFavorite?.let { save(it) }
            return deviceName
        }
    }

    fun modifyDeviceName(username: String, deviceName: String) {
        val device = deviceRepository.findDeviceEntityByNameAndOwnerUsername(username, deviceName)
        device?.apply {
            name = deviceName
            deviceRepository.save(this)
        }
    }

    private fun buildJson(shadowValues: Map<String, String>): String {
        val stateObject = ObjectMapper().createObjectNode()
        val desiredNode = ObjectMapper().createObjectNode()
        val attributeNode = ObjectMapper().createObjectNode()
        shadowValues.forEach { attr, value ->
            attributeNode.put(attr, value)
        }
        desiredNode.putPOJO("desired", attributeNode)
        stateObject.putPOJO("state", desiredNode)
        return stateObject.toString()
    }

    private fun connect(device: AWSIotDevice) {
        if (broker.connectionStatus == AWSIotConnectionStatus.DISCONNECTED) {
            println("Not connected yet --------> connecting ----------> NOW")
            device.reportInterval = 5000L
            broker.keepAliveInterval = 20_000
            broker.attach(device)
            broker.connect()
        }
        println("==========connected=================")
    }

}