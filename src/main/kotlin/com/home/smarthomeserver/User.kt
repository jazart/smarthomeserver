package com.home.smarthomeserver

import java.util.*
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

data class PlantMoisture(val name: String = "",
                         val reading: MoistureReading
                         = MoistureReading.Medium(ThreadLocalRandom.current().nextLong()))


data class DeviceInformation(val name: String = "device",
                             val status: String = "--")


//class com.home.smarthomeserver.devices.Light() : Device {
//    override var name: String
//        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//        set(value) {}
//    override val deviceID: String
//        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//    override val status: Status
//        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//}
//
//class Thermostat() : Device {
//    override var name: String
//        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//        set(value) {}
//    override val deviceID: String
//        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//    override val status: Status
//        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//}

sealed class Temperature(val temp: Double) {
    class Low(temp: Double) : Temperature(temp)
    class Medium(temp: Double) : Temperature(temp)
    class High(temp: Double) : Temperature(temp)

}

sealed class MoistureReading(readingLevel: Long) {
    data class High(val readingLevel: Long) : MoistureReading(readingLevel)
    data class Medium(val readingLevel: Long) : MoistureReading(readingLevel)
    data class Low(val readingLevel: Long) : MoistureReading(readingLevel)
}

enum class Status {
    ON,
    OFF
}
