package com.home.smarthomeserver

import java.util.UUID
import java.util.concurrent.ThreadLocalRandom

open class Device{
    open val info: DeviceInformation = DeviceInformation()
    open var name: String = ""
    open val id: String = ""
    open val status: Status = Status.OFF
}

data class User(val name: String = "User Value goes here",
                val id: String = UUID.randomUUID().toString(),
                val family: Array<User> = arrayOf(),
                val info: DeviceInformation = DeviceInformation()
                )


data class DeviceInformation(val name: String = "device",
                             val status: String = "--")


enum class Status {
    ON,
    OFF
}
