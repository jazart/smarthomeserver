package com.home.smarthomeserver.controllers

import com.amazonaws.services.iot.client.AWSIotConnectionStatus
import com.home.smarthomeserver.Status
import com.home.smarthomeserver.devices.RPILight
import com.home.smarthomeserver.models.Command
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable

@Controller
class DeviceController {

    @Autowired
    lateinit var broker: AwsBroker
    val device = RPILight("MyRaspberryPi")

    fun connect() {
        if (broker.client.connectionStatus == AWSIotConnectionStatus.DISCONNECTED) {
            println("Not connected yet --------> connecting ----------> NOW")
            device.reportInterval = 1000L
            broker.client.keepAliveInterval = 30_000
            broker.client.attach(device)
            broker.client.connect()
        }
    }

    fun updateDeviceStatus(@PathVariable("id") id: String, deviceName: String, command: Command) {
        device.command = command.toString()
        device.update("{ " +
                "\"state\" : { " +
                "                       \"desired\": {" +
                "                           \"command\": \"$command\"} " +
                "                     } " +
                "               }")
        println("==========connected=================")
    }
}

