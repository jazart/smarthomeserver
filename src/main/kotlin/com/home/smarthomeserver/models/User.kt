package com.home.smarthomeserver.models

import java.util.*
import javax.persistence.*

interface User {

    val id: Long
        get() = 0

    val name: String
        get() = ""

    val password: String
        get() = ""

}


@Entity(name = "parent_table")
data class ParentUser(override var name: String = "Jaida Payne",

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