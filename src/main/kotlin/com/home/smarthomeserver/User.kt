package com.home.smarthomeserver

import com.home.smarthomeserver.models.Command
import java.util.*

open class Device {
    open val info: DeviceInformation = DeviceInformation()
    open var name: String = ""
    open val id: String = ""
    open val status: Status = Status.DISCONNECTED
}

data class User(val name: String = "Jeremy",
                val id: String = UUID.randomUUID().toString(),
                val family: List<User> = listOf(),
                val info: DeviceInformation = DeviceInformation(),
                val devices: List<Device> = mutableListOf(Light())
)


data class DeviceInformation(val name: String = "device",
                             val status: String = "--",
                             val commands: List<Command> = listOf())

data class Light(override var name: String = "Light",
                 override val info: DeviceInformation = DeviceInformation(commands =
                 listOf(Command.PULSE, Command.TURN_OFF, Command.TURN_ON))
) : Device()

enum class Status {
    CONNECTED,
    DISCONNECTED
}
