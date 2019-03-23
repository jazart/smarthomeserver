package com.home.smarthomeserver.service

import com.amazonaws.services.iot.client.AWSIotConnectionStatus
import com.amazonaws.services.iot.client.AWSIotDevice
import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.smarthomeserver.repository.DeviceRepository
import com.home.smarthomeserver.awsconfig.AwsIotClient
import com.home.smarthomeserver.devices.RPILight
import com.home.smarthomeserver.entity.DeviceEntity
import com.home.smarthomeserver.entity.Status
import com.home.smarthomeserver.models.Command
import com.home.smarthomeserver.models.DeviceInfo
import kotlinx.coroutines.future.await
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
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

    fun updateDeviceStatus(deviceInfo: DeviceInfo, command: Command) {
        val device = RPILight(deviceInfo.deviceName)
        connect(device)
        device.command = command.toString()
        device.status = Status.CONNECTED.toString()
        val json = buildJson(mapOf("command" to "$command"))
        device.update(json)
    }

    @Throws(Exception::class)
    suspend fun addDevice(deviceInfo: DeviceInfo): Boolean {
        val newThingName = deviceInfo.deviceName +
                userService.userRepository.findUserByUsername(deviceInfo.username).id
        val thingResponse = AwsIotClient.get().createThing(CreateThingRequest.builder().run {
            thingName(newThingName)
            build()
        }).await()
        try {
            if (thingResponse.sdkHttpResponse().isSuccessful) {
                val user = userService.getUserEntity(deviceInfo.username)
                val deviceEntity = DeviceEntity(
                        name = deviceInfo.deviceName,
                        commands = mutableListOf(),
                        id = 1L, owner = user, thingName = newThingName)
                user.devices.add(deviceEntity)
                userService.userRepository.save(user)
            }
        } catch (e: DataIntegrityViolationException) {
            throw Exception("User: '${deviceInfo.username}' already has a device named '${deviceInfo.deviceName}'")
        }
        return thingResponse.sdkHttpResponse().isSuccessful
    }


    suspend fun removeDevice(deviceInfo: DeviceInfo): Boolean {
        val deviceToDelete = deviceRepository.findDeviceEntityByNameAndOwnerUsername(deviceInfo.deviceName, deviceInfo.username)
        deviceToDelete?.run {
            deviceRepository.delete(this)
            val result = AwsIotClient.get().deleteThing(
                    DeleteThingRequest.builder().thingName(thingName).build()
            ).await().sdkHttpResponse().isSuccessful
            return result
        }
        return false
    }

    fun addFavorite(deviceInfo: DeviceInfo): String {
        deviceRepository.apply {
            val prevFavorite = findDeviceEntityByFavoriteTrueAndOwnerUsername(deviceInfo.username)
            val newFavorite = findDeviceEntityByNameAndOwnerUsername(deviceInfo.deviceName, deviceInfo.username)
                    ?: throw Exception()
            if (prevFavorite == null) {
                newFavorite.favorite = true
                save(newFavorite)
            } else {
                prevFavorite.favorite = false
                newFavorite.favorite = true
                saveAll(listOf(prevFavorite, newFavorite))
            }
        return deviceInfo.deviceName
        }
    }

    fun removeFavorite(deviceInfo: DeviceInfo): String {
        deviceRepository.apply {
            val prevFavorite = findDeviceEntityByNameAndOwnerUsername(deviceInfo.deviceName, deviceInfo.username)
            prevFavorite?.favorite = false
            prevFavorite?.let { save(it) }
            return deviceInfo.deviceName
        }
    }

    fun modifyDeviceName(deviceInfo: DeviceInfo, newName: String): String {
        val device = deviceRepository.findDeviceEntityByNameAndOwnerUsername(deviceInfo.deviceName,deviceInfo.username)
        device?.apply {
            name = newName
            deviceRepository.save(this)
            return newName
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

//    private fun validateCreds
}