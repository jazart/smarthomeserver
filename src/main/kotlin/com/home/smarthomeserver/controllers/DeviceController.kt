package com.home.smarthomeserver.controllers

import com.amazonaws.services.iot.client.AWSIotConnectionStatus
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.home.smarthomeserver.*
import com.home.smarthomeserver.devices.LedLight
import com.home.smarthomeserver.devices.Light
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ThreadLocalRandom

@RestController
class DeviceController {

    @Autowired
    lateinit var broker: AwsBroker
    val device = LedLight("MyPc")
    @RequestMapping("/device/{id}", method = [RequestMethod.GET])
    fun updateDeviceStatus(@PathVariable("id") id: String) {
        if (broker.client.connectionStatus == AWSIotConnectionStatus.DISCONNECTED) {
            println("Not connected yet --------> connecting ----------> NOW")
            device.reportInterval = 5000L
            broker.client.keepAliveInterval = 30_000
            broker.client.attach(device)
            broker.client.connect()
        } else {
            device.power = ThreadLocalRandom.current().nextInt()
            println("==========connected=================")
        }
    }
}
