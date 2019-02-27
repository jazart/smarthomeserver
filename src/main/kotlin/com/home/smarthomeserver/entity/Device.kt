package com.home.smarthomeserver.entity

import com.home.smarthomeserver.models.Command
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

enum class Status {
    CONNECTED,
    DISCONNECTED
}
