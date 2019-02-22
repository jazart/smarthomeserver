package com.home.smarthomeserver

import com.home.smarthomeserver.models.Command
import java.util.UUID

open class Device{
    open val info: DeviceInformation = DeviceInformation()
    open var name: String = ""
    open val id: String = ""
    open val status: Status = Status.DISCONNECTED
}

data class User(val name: String = "User Value goes here",
                val id: String = UUID.randomUUID().toString(),
                val family: List<User> = listOf(),
                val info: DeviceInformation = DeviceInformation(),
                val devices: List<Device> = mutableListOf()
                )


data class DeviceInformation(val name: String = "device",
                             val status: String = "--",
                             val commands: List<Command> = listOf())


enum class Status {
    CONNECTED,
    DISCONNECTED
}
