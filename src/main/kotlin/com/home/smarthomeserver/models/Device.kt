package com.home.smarthomeserver.models


open class Device(
        open val info: DeviceInformation = DeviceInformation(),
        open var name: String = "",
        open val id: String = "",
        open val status: Status = Status.DISCONNECTED
)

data class DeviceInformation(val name: String = "device",
                             val status: String = "--",
                             val commands: List<Command> = listOf())


data class Light(override var name: String = "Light",
                 override val info: DeviceInformation = DeviceInformation(commands = listOf(Command.PULSE, Command.TURN_OFF, Command.TURN_ON))
) : Device()

enum class Status {
    CONNECTED,
    DISCONNECTED
}
