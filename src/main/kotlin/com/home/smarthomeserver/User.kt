package com.home.smarthomeserver

import java.util.concurrent.ThreadLocalRandom

open class Device{
    open var name: String = ""
    open val id: String = ""
    open val status: Status = Status.OFF
}

data class User(val name: String = "",
                val id: String = "",
                val status: String = "")

data class PlantMoisture(val name: String = "",
                         val reading: MoistureReading
                            = MoistureReading.Medium(ThreadLocalRandom.current().nextLong()))

//class Light() : Device {
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
