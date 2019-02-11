package com.home.smarthomeserver.controllers

import com.amazonaws.services.iot.client.AWSIotConnectionStatus
import com.home.smarthomeserver.Status
import com.home.smarthomeserver.devices.LedLight
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class DeviceController {

    @Autowired
    lateinit var broker: AwsBroker
    val device = LedLight("MyPc")

    fun connect() {
        if (broker.client.connectionStatus == AWSIotConnectionStatus.DISCONNECTED) {
            println("Not connected yet --------> connecting ----------> NOW")
            device.reportInterval = 5000L
            broker.client.keepAliveInterval = 30_000
            broker.client.attach(device)
            broker.client.connect()
        }
    }

    @RequestMapping("/device/{id}", method = [RequestMethod.GET])
    fun updateDeviceStatus(@PathVariable("id") id: String, deviceName: String, command: Status) {
        device.status = command.name
        println("==========connected=================")
    }
}

