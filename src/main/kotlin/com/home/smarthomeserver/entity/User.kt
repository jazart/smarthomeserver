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

        @Column(updatable = false, nullable = false)
        override val firstName: String = "",

        @Column(updatable = false, nullable = false)
        override val lastName: String = "",

        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        override var id: Long,

        @OneToMany(cascade = [CascadeType.MERGE, CascadeType.REFRESH], fetch = FetchType.EAGER)
        var family: MutableList<ChildUserEntity> = mutableListOf(),

        @OneToMany(cascade = [CascadeType.MERGE, CascadeType.REFRESH], fetch = FetchType.LAZY)
        @Column(nullable = false)
        var devices: MutableList<DeviceEntity> = mutableListOf(),

        @Column(nullable = false, unique = true)
        override val email: String,

        override var password: String) : User

@Entity(name = "child_table")
data class ChildUserEntity(
        @Column(unique = true, updatable = false)
        override val username: String,

        @Column(updatable = false, nullable = false)
        override val firstName: String = "",

        @Column(updatable = false, nullable = false)
        override val lastName: String = "",

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        override var id: Long,

        @Column(nullable = false, unique = true)
        override val email: String,

        override var password: String,

        @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.REFRESH])
        @JoinColumn(name = "familyId")
        var parent: ParentUserEntity) : User

fun ParentUserEntity.toUserDomain(): ParentUser {
    val parentUserDomain = ParentUser(
            firstName = this.firstName,
            lastName = this.lastName,
            username = this.username
    )

    val domainDevices = this.devices.map { dev -> dev.toDeviceDomain(parentUserDomain) }.toMutableList()
    parentUserDomain.devices.addAll(domainDevices)
    val familyDomain = this.family.map { fam -> fam.toUserDomain(parentUserDomain) }.toMutableList()
    parentUserDomain.family.addAll(familyDomain)
    return parentUserDomain
}

fun ChildUserEntity.toUserDomain(parent: ParentUser) = ChildUser(
        firstName = this.firstName,
        lastName = this.lastName,
        parent = parent,
        username = this.username
)

interface User {

    val id: Long
        get() = 0

    val firstName: String
        get() = ""

    val lastName: String
        get() = ""

    val username: String
        get() = ""
    val password: String
        get() = ""

    val email: String
        get() = ""


}