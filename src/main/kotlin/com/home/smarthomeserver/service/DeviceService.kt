package com.home.smarthomeserver.service

import com.amazonaws.services.iot.client.AWSIotConnectionStatus
import com.amazonaws.services.iot.client.AWSIotDevice
import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.amazonaws.services.iot.client.AWSIotQos
import com.fasterxml.jackson.databind.ObjectMapper
import com.home.smarthomeserver.awsconfig.AwsIotClient
import com.home.smarthomeserver.devices.PiCamera
import com.home.smarthomeserver.devices.RPILight
import com.home.smarthomeserver.entity.DeviceEntity
import com.home.smarthomeserver.models.Command
import com.home.smarthomeserver.models.DeviceInfo
import com.home.smarthomeserver.models.DeviceType
import com.home.smarthomeserver.repository.DeviceRepository
import kotlinx.coroutines.future.await
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import software.amazon.awssdk.services.iot.model.CreateThingRequest
import software.amazon.awssdk.services.iot.model.DeleteThingRequest


/**
 * Performs business logic by taking data passed from the top level mutation resolver and interacting with the DB and other
 * service level classes. All methods in this class are transactional, meaning if the method does not return then any db modification
 * is rolled back.
 */
@Service
@Transactional
class DeviceService {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var broker: AWSIotMqttClient

    @Autowired
    lateinit var deviceRepository: DeviceRepository

    /**
     * Updates the status of a device. First checks the type and updates the device's shadow state according to the given
     * command.
     */
    fun updateDeviceStatus(deviceInfo: DeviceInfo, command: Command) {
        val device = deviceRepository.findDeviceEntityByNameAndOwnerUsername(
                deviceInfo.deviceName, deviceInfo.username) ?: throw Exception("Device not found")

        val client = AWSIotMqttClient(System.getenv("AWS_CLIENT_ENDPOINT"),
                System.getenv("AWS_CLIENT_ID"),
                System.getenv("AWS_ACCESS_KEY_ID"),
                System.getenv("AWS_SECRET_KEY")
        )

        if (deviceInfo.type == DeviceType.LIGHT) {
            updateLightCommand(command, RPILight(device.thingName), client)
        }
        val thing = PiCamera(device.thingName)
        thing.shadowUpdateQos = AWSIotQos.QOS1
        if (command == Command.SNAP) {
            connect(thing, 0L, client)
            thing.update(buildJson(mapOf("url" to ""), mapOf("camera" to true, "stream" to false)))
            thing.camera = true
            thing.stream = false
        } else if (command == Command.STREAM) {
            sendStreamCommand(deviceInfo, command, client)
        }
        cleanup(client)
    }

    // Updates the Light thing to the given command sent by the user
    fun updateLightCommand(command: Command, thing: RPILight, client: AWSIotMqttClient) {
        connect(thing, 0L, client)
        thing.update(buildJson(null, mapOf("command" to command.name)))
    }


    /**
     * Updates the thing shadow to an appropriate state to begin a stream on the thing. Once the stream begins a link to
     * the stream is returned
     */
    fun sendStreamCommand(deviceInfo: DeviceInfo, command: Command, client: AWSIotMqttClient): String {
        val device = deviceRepository.findDeviceEntityByNameAndOwnerUsername(
                deviceInfo.deviceName, deviceInfo.username) ?: throw Exception("Device not found")
        val thing = PiCamera(device.thingName)
        connect(thing, 0L, client)
        thing.shadowUpdateQos = AWSIotQos.QOS1
        thing.update(buildJson(null, mapOf("stream" to true, "camera" to false)))
        println("================= ${thing.url} ============")
        var streamUrl = ""

        while (streamUrl.isBlank()) {
            streamUrl = String(thing.url.toByteArray())
            thing.url = ""
            thing.camera = false
        }
        cleanup(client)
        return streamUrl
    }

    private fun connect(device: AWSIotDevice, reportInterval: Long, client: AWSIotMqttClient) {
        device.reportInterval = reportInterval
        device.deviceReportQos = AWSIotQos.QOS1
        device.methodAckQos = AWSIotQos.QOS1
        device.methodQos = AWSIotQos.QOS1


        client.serverAckTimeout = 5000
        client.connectionTimeout = 10000
        client.attach(device)

        if (client.connectionStatus == AWSIotConnectionStatus.DISCONNECTED) {
            println("Not connected yet --------> connecting ----------> NOW")
            client.connect()
            println("==========connected=================")
        }

    }

    /**
     * Creates a thing name based on the name given by the user and then concatenated with the user's internal DB id. AWS
     * requires all thing names be unique. This ensures different users can have the same thing name. This method adds the device to the db
     * then creates a thing shadow on AWS. If creation of the thing fails, the db transaction is aborted.
     */
    @Throws(Exception::class)
    suspend fun addDevice(deviceInfo: DeviceInfo): Boolean {
        val newThingName = deviceInfo.deviceName.plus(userService.userRepository.findUserByUsername(deviceInfo.username)?.id).trim()
                .toSnakeCase()
        val thingResponse = AwsIotClient.get().createThing(CreateThingRequest.builder().run {
            thingName(newThingName)
            build()
        }).await()
        try {
            if (thingResponse.sdkHttpResponse().isSuccessful) {
                val user = userService.getUserEntity(deviceInfo.username)
                val deviceEntity = DeviceEntity(
                        name = deviceInfo.deviceName,
                        commands = deviceInfo.command,
                        favorite = deviceInfo.isFavorite,
                        id = 1L, owner = user, thingName = newThingName, type = deviceInfo.type)
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

    fun modifyDeviceName(deviceInfo: DeviceInfo, newName: String): String? {
        val device = deviceRepository.findDeviceEntityByNameAndOwnerUsername(deviceInfo.deviceName, deviceInfo.username)
        device?.apply {
            name = newName
            deviceRepository.save(this)
            return newName
        }
        return null
    }

    /**
     * Builds the state json for an AWS thing shadow.
     */
    private fun buildJson(shadowValues: Map<String, String>?, reportedValues: Map<String, Any>): String {
        val stateObject = ObjectMapper().createObjectNode()
        val attributeNode = ObjectMapper().createObjectNode()
        val reportedAttrs = ObjectMapper().createObjectNode()
        shadowValues?.forEach { (attr, value) ->
            attributeNode.put(attr, value)
        }
        reportedValues.forEach { (attr, value) ->
            when (value) {
                is String -> reportedAttrs.put(attr, value)
                is Boolean -> reportedAttrs.put(attr, value)
            }
        }
        stateObject.putPOJO("state", ObjectMapper().createObjectNode()
                        .putPOJO("desired", attributeNode)
                        .putPOJO("reported", reportedAttrs))
        return stateObject.toString()
    }

    fun cleanup(client: AWSIotMqttClient) {
        client.disconnect()
    }

}

fun String.toSnakeCase(): String {
    return this.replace(" ", "_")
}