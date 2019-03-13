package com.home.smarthomeserver

import com.amazonaws.services.iot.client.AWSIotConnectionStatus
import com.home.smarthomeserver.controllers.AwsBroker
import com.home.smarthomeserver.devices.RPILight
import com.home.smarthomeserver.entity.Status
import com.home.smarthomeserver.models.Command
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
class DeviceService {
    @Autowired
    lateinit var broker: AwsBroker

    @Autowired
    lateinit var deviceRepository: DeviceRepository

    val device = RPILight("MyRaspberryPi")
    val device2 = RPILight("TestCreation")

    fun connect() {
        if (broker.client.connectionStatus == AWSIotConnectionStatus.DISCONNECTED) {
            println("Not connected yet --------> connecting ----------> NOW")
            device.reportInterval = 5000L
            broker.client.keepAliveInterval = 30_000
            broker.client.attach(device)
            broker.client.attach(device2)
            broker.client.connect()
        }
    }

    fun updateDeviceStatus(id: String, deviceName: String, command: Command) {
        device.command = command.toString()
        device.status = Status.DISCONNECTED.toString()
        device.update("{ " +
                "\"state\" : { " +
                "                       \"desired\": {" +
                "                           \"command\": \"$command\"} " +
                "                     } " +
                "               }")

        device2.command = command.toString()
        device2.status = Status.DISCONNECTED.toString()
        device2.update("{ " +
                "\"state\" : { " +
                "                       \"desired\": {" +
                "                           \"command\": \"$command\"} " +
                "                     } " +
                "               }")
        println("==========connected=================")
    }

    fun removeDevice(id: String){
        deviceRepository.delete(deviceRepository.findDeviceEntityById(id.toLong())!!)
    }

    fun modifyDName(id: String, deviceName: String){
        deviceRepository.findDeviceEntityById(id.toLong())!!.name = deviceName
    }
}