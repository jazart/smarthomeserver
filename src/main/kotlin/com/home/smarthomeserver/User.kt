package com.home.smarthomeserver

import java.util.UUID

open class Device{
    open val info: DeviceInformation = DeviceInformation()
    open var name: String = ""
    open val id: String = ""
    open val status: Status = Status.OFF
}

data class User(val name: String = "User Value goes here",
                val id: String = UUID.randomUUID().toString(),
                val family: List<User> = listOf(),
                val info: DeviceInformation = DeviceInformation(),
                val devices: List<Device> = mutableListOf()
                )


data class DeviceInformation(val name: String = "device",
                             val status: String = "--")


enum class Status {
    ON,
    OFF
}
