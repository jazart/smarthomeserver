package com.home.smarthomeserver

import com.amazonaws.services.iot.client.AWSIotConnectionStatus
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
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import software.amazon.awssdk.services.iot.model.AttributePayload
import software.amazon.awssdk.services.iot.model.CreateThingRequest

@Service
class DeviceService {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var broker: AWSIotMqttClient

    @Autowired
    lateinit var deviceRepository: DeviceRepository

    val device = RPILight("MyRaspberryPi")

    fun connect() {
        if (broker.connectionStatus == AWSIotConnectionStatus.DISCONNECTED) {
            println("Not connected yet --------> connecting ----------> NOW")
            device.reportInterval = 5000L
            broker.keepAliveInterval = 30_000
            broker.attach(device)
            broker.connect()
        }
        println("==========connected=================")
    }

    fun updateDeviceStatus(id: String, deviceName: String, command: Command) {
        device.command = command.toString()
        device.status = Status.DISCONNECTED.toString()
        val json = buildJson(mapOf("command" to "$command"))
        device.update(json)
    }

    @Transactional(propagation = Propagation.REQUIRED)
    suspend fun addDevice(username: String, device: String): Boolean {
        val thingResponse = AwsIotClient.get().createThing(CreateThingRequest.builder().run {
            thingName(device)
            attributePayload(AttributePayload.builder().attributes(mapOf("message" to "yeet"))
                    .build()
            )
            build()
        }).await()
        if (thingResponse.sdkHttpResponse().isSuccessful) {
            val user = userService.getUserEntity(username, true)
            val deviceEntity = DeviceEntity(name = device, commands = mutableListOf(), id = 1L, owner = user)
            user.devices.add(deviceEntity)
            userService.userRepository.save(user)
        }
        return thingResponse.sdkHttpResponse().isSuccessful
    }

    @Transactional
    fun removeDevice(username: String, deviceName: String) {
        val user = userService.getUserEntity(username)
        user.apply {
            val deviceToRemove = devices.first { device -> device.name == deviceName }
            devices.remove(deviceToRemove)
            userService.userRepository.save(this)
            deviceRepository.findDeviceEntityById(deviceToRemove.id)?.let { deviceRepository.delete(it) }
        }
    }

    fun modifyDeviceName(id: String, deviceName: String) {
        deviceRepository.findDeviceEntityById(id.toLong())?.name = deviceName
        deviceRepository.findDeviceEntityById(id.toLong())?.let { device ->
            deviceRepository.save(device)
        }
    }

    fun buildJson(shadowValues: Map<String, String>): String {
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
}