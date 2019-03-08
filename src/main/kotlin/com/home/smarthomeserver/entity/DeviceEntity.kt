package com.home.smarthomeserver.entity

import com.home.smarthomeserver.models.Command
import com.home.smarthomeserver.models.Device
import com.home.smarthomeserver.models.Light
import javax.persistence.*


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Embeddable
open class DeviceEntity(
        open var name: String = "",

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        open val id: Long,

        @Enumerated(EnumType.STRING)
        open var status: Status = Status.DISCONNECTED,

        @ElementCollection
        open var commands: MutableList<Command> = mutableListOf()
)


@Entity
data class LightEntity(override var name: String = "LightEntity",
                       override var commands: MutableList<Command> = mutableListOf(Command.PULSE, Command.TURN_OFF, Command.TURN_ON),

                       @Id @GeneratedValue(strategy = GenerationType.AUTO)
                       override val id: Long)
    : DeviceEntity(name, commands = commands, id = id)

fun DeviceEntity.toDeviceDomain() = Device(
        name = this.name,
        status = this.status,
        commands = this.commands,
        id = this.id
)

fun LightEntity.toDeviceDomain() = Light (
        name = this.name,
        commands = this.commands,
        id = this.id
)

enum class Status {
    CONNECTED,
    DISCONNECTED
}
