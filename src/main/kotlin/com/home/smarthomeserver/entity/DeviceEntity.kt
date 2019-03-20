package com.home.smarthomeserver.entity

import com.home.smarthomeserver.models.Command
import com.home.smarthomeserver.models.Device
import com.home.smarthomeserver.models.Light
import com.home.smarthomeserver.models.ParentUser
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
        open var status: Status = Status.DISCONNECTED,

        @Enumerated(EnumType.STRING)
        @ElementCollection(targetClass = Command::class)
        open var commands: MutableList<Command> = mutableListOf(),

        @ManyToOne(cascade = [CascadeType.REFRESH, CascadeType.MERGE])
        @JoinColumn(name = "parent_id")
        open var owner: ParentUserEntity,

        @Column(nullable = false)
        open var favorite: Boolean = false,

        @Column(nullable = false, unique = true, updatable = false)
        open var thingName: String

)


@Entity
data class LightEntity(override var name: String,
                       override var commands: MutableList<Command> = mutableListOf(Command.PULSE, Command.TURN_OFF, Command.TURN_ON),

                       @Id @GeneratedValue(strategy = GenerationType.AUTO)
                       override val id: Long,

                       @Column(nullable = false)
                       override var owner: ParentUserEntity,
                       override var thingName: String)
    : DeviceEntity(name, commands = commands, id = id, owner = owner, thingName = thingName)

fun DeviceEntity.toDeviceDomain(owner: ParentUser) = Device(
        name = this.name,
        status = this.status,
        commands = this.commands,
        id = this.id,
        owner = owner
)

fun LightEntity.toDeviceDomain() = Light(
        name = this.name,
        commands = this.commands,
        id = this.id
)

enum class Status {
    CONNECTED,
    DISCONNECTED
}
