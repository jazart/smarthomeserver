package com.home.smarthomeserver.devices

import com.amazonaws.services.iot.client.AWSIotDevice
import com.amazonaws.services.iot.client.AWSIotDeviceProperty

abstract class BaseIotDevice(thingName: String, @field:AWSIotDeviceProperty open var type: String) : AWSIotDevice(thingName)
