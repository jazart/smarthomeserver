package com.home.smarthomeserver.devices

import com.amazonaws.services.iot.client.AWSIotDevice
import com.amazonaws.services.iot.client.AWSIotDeviceProperty

abstract class BaseIotDevice(@field:AWSIotDeviceProperty open var type: String, thingName: String) : AWSIotDevice(thingName)
