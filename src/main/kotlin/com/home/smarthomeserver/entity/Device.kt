package com.home.smarthomeserver.entity

import com.home.smarthomeserver.models.Command
import com.home.smarthomeserver.models.DeviceView
import com.home.smarthomeserver.models.LightView
import javax.persistence.*


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Embeddable
open class Device(
        open var name: String = "",

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        open val id: Long = 0,

        @Enumerated(EnumType.STRING)
        open val status: Status = Status.DISCONNECTED,

        @ElementCollection
        open val commands: MutableList<Command> = mutableListOf()
)


@Entity
data class Light(override var name: String = "Light",
                 override val commands: MutableList<Command> = mutableListOf(Command.PULSE, Command.TURN_OFF, Command.TURN_ON))
    : Device(name, commands = commands)

fun Device.toDeviceDomain() = DeviceView(
        name = this.name,
        status = this.status,
        commands = this.commands
)

fun Light.toDeviceDomain() = LightView (
        name = this.name,
        commands = this.commands
)
enum class Status {
    CONNECTED,
    DISCONNECTED
}
