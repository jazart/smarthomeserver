package com.home.smarthomeserver.devices

import com.amazonaws.services.iot.client.AWSIotDeviceProperty
import com.home.smarthomeserver.models.DeviceType

class RPILight(name: String,
               @field:AWSIotDeviceProperty var status: String = "",
               @field:AWSIotDeviceProperty var command: String = "",
               override var type: String = DeviceType.LIGHT.name) : BaseIotDevice(name, type) {

    override fun onShadowUpdate(jsonState: String?) {
        super.onShadowUpdate(jsonState)
        println("$thingName just got a message with the following content: $jsonState")
    }
}