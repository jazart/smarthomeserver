package com.home.smarthomeserver.model

import com.home.smarthomeserver.models.Device
import com.home.smarthomeserver.models.DeviceInformation
import com.home.smarthomeserver.models.Light
import java.util.*


data class User(val name: String = "Jeremy",
                val id: String = UUID.randomUUID().toString(),
                val family: List<User> = listOf(),
                val info: DeviceInformation = DeviceInformation(),
                val devices: List<Device> = mutableListOf(Light())
)