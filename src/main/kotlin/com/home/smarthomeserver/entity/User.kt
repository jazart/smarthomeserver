package com.home.smarthomeserver.entity

import com.home.smarthomeserver.models.ChildUser
import com.home.smarthomeserver.models.ParentUser
import javax.persistence.*


/**
 * These are currently model objects that are a 1-1 match to what is in our database. We would like to
 * crete a separate but similar version of these objects but without the extra info that we do not wish
 * to send to the client (eg. password, id). To accomplish this. We should prefix these classes with
 * "Entity" to signify they are database entities. Then we should create a model representation of these classes
 * that do not have sensitive data like id and password. Simply use a mapper to map from the entitiy to domain
 * For example the following class:
 *
 *  data class ParentUserEntity(id, name, password, children...) should be mapped -->
 *  -->data class ParentUserEntity(name, children, ...)
 *  This can be accomplished by creating a mapper class, creating a method called map() that takes an entity object
 *  and returns a model object.
 */

@Entity(name = "parent_table")
data class ParentUserEntity(
        @Column(unique = true, updatable = false)
        override val username: String,

        override var name: String = "J Payne",

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        override var id: Long,

        @OneToMany(cascade = [CascadeType.MERGE, CascadeType.REFRESH], fetch = FetchType.EAGER)
        var family: MutableList<ChildUserEntity> = mutableListOf(),

        @ElementCollection
        @Column(nullable = false)
        var devices: MutableList<DeviceEntity> = mutableListOf(),

        override var password: String) : User

@Entity(name = "child_table")
data class ChildUserEntity(
        @Column(unique = true, updatable = false)
        override val username: String,
        override var name: String,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        override var id: Long,

        override var password: String,

        @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.REFRESH])
        @JoinColumn(name = "familyId")
        var parent: ParentUserEntity) : User

fun ParentUserEntity.toUserDomain(): ParentUser {
    val domainDevices = this.devices.map { dev -> dev.toDeviceDomain() }.toMutableList()
    val familyDomain = this.family.map { fam -> fam.toUserDomain() }.toMutableList()
    return ParentUser(
            name = this.name,
            family = familyDomain,
            devices = domainDevices,
            username = this.username
    )
}

fun ChildUserEntity.toUserDomain() = ChildUser(
        name = this.name,
        parent = this.parent.toUserDomain(),
        username = this.username
)

interface User {

    val id: Long
        get() = 0

    val name: String
        get() = ""

    val username: String
        get() = ""
    val password: String
        get() = ""

}