package com.home.smarthomeserver.devices

import com.amazonaws.services.iot.client.AWSIotDevice
import com.amazonaws.services.iot.client.AWSIotDeviceProperty

class PiCamera(name: String,
               @field:AWSIotDeviceProperty var video: Boolean = true,
               @field:AWSIotDeviceProperty var camera: Boolean = true) : AWSIotDevice(name)