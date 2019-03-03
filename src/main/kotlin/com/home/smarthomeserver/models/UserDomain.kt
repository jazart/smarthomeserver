package com.home.smarthomeserver.models

import com.home.smarthomeserver.entity.*

data class ParentUserView  (

    val name: String,
    val family: MutableList<ChildUser> = mutableListOf(),
    val devices: MutableList<Device> = mutableListOf(Light())
    )

data class ChildUserView  (

        val name: String,
        val parent: ParentUser

)