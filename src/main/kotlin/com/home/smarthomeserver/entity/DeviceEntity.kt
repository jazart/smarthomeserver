package com.home.smarthomeserver.entity

import com.home.smarthomeserver.models.*
import javax.persistence.*


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(
        uniqueConstraints = [UniqueConstraint(columnNames = ["name", "parent_id"])]
)
open class DeviceEntity(
        open var name: String = "",

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        open val id: Long,

        @Enumerated(EnumType.STRING)
        open var status: Status = Status.CONNECTED,

        @Enumerated(EnumType.STRING)
        @ElementCollection(targetClass = Command::class)
        open var commands: MutableList<Command> = mutableListOf(),

        @ManyToOne
        @JoinColumn(name = "parent_id")
        open var owner: ParentUserEntity,

        @Column(nullable = false)
        open var favorite: Boolean = false,

        @Column(nullable = false, unique = true, updatable = false)
        open var thingName: String,

        @Enumerated(EnumType.STRING)
        open var type: DeviceType?

)


@Entity
data class LightEntity(override var name: String,
                       override var commands: MutableList<Command> = mutableListOf(Command.PULSE, Command.TURN_OFF, Command.TURN_ON),

                       @Id @GeneratedValue(strategy = GenerationType.AUTO)
                       override val id: Long,

                       @Column(nullable = false)
                       override var owner: ParentUserEntity,
                       override var thingName: String)
    : DeviceEntity(name, commands = commands, id = id, owner = owner, thingName = thingName, type = DeviceType.LIGHT)

fun DeviceEntity.toDeviceDomain(owner: ParentUser) = Device(
        name = this.name,
        status = this.status,
        commands = this.commands,
        owner = owner.username,
        isFavorite = favorite,
    type = this.type ?: DeviceType.CAMERA
)

fun LightEntity.toDeviceDomain() = Light(
        name = this.name,
        commands = this.commands
)

enum class Status {
    CONNECTED,
    DISCONNECTED
}
