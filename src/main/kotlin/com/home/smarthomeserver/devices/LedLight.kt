package com.home.smarthomeserver.devices

import com.amazonaws.services.iot.client.AWSIotDevice
import com.amazonaws.services.iot.client.AWSIotDeviceProperty

class LedLight(name: String, @field:AWSIotDeviceProperty var status: String = "",
               @field:AWSIotDeviceProperty var power: Int = 0,
               @field:AWSIotDeviceProperty var batt: Int = 0) : AWSIotDevice(name) {


    override fun onShadowUpdate(jsonState: String?) {
        super.onShadowUpdate(jsonState)
        println("$thingName just got a message with the following content: $jsonState")
    }

}