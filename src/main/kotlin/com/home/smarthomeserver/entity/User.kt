package com.home.smarthomeserver.entity

import com.home.smarthomeserver.models.ChildUserView
import com.home.smarthomeserver.models.ParentUserView
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
 *  -->data class ParentUser(name, children, ...)
 *  This can be accomplished by creating a mapper class, creating a method called map() that takes an entity object
 *  and returns a model object.
 */

@Entity(name = "parent_table")
data class ParentUser(override var name: String = "J Payne",

                      @Id @GeneratedValue(strategy = GenerationType.AUTO)
                      override var id: Long = 0,

                      @OneToMany(cascade = [CascadeType.MERGE, CascadeType.REFRESH], fetch = FetchType.EAGER) var family: MutableList<ChildUser> = mutableListOf(),

                      @Transient
                      @ElementCollection
                      var devices: MutableList<Device> = mutableListOf(Light()),

                      override var password: String) : User

@Entity(name = "child_table")
data class ChildUser(override var name: String,

                     @Id
                     @GeneratedValue(strategy = GenerationType.AUTO)
                     override var id: Long = 0,

                     override var password: String,

                     @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.REFRESH])
                     @JoinColumn(name = "familyId")
                     var parent: ParentUser) : User

fun ParentUser.toUserDomain() = ParentUserView(
        name = this.name,
        family = this.family,
        devices = this.devices
)

fun ChildUser.toUserDomain() = ChildUserView(
        name = this.name,
        parent = this.parent
)

interface User {

    val id: Long
        get() = 0

    val name: String
        get() = ""

    val password: String
        get() = ""

}