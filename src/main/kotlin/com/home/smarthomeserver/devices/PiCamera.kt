package com.home.smarthomeserver.devices

import com.amazonaws.services.iot.client.AWSIotDeviceProperty
import com.home.smarthomeserver.models.DeviceType

class PiCamera(name: String,
               @field:AWSIotDeviceProperty var camera: Boolean = false,
               @field:AWSIotDeviceProperty var stream: Boolean = false,
               override var type: String = DeviceType.CAMERA.name) : BaseIotDevice(name, type)