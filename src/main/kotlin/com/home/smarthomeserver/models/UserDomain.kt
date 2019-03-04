package com.home.smarthomeserver.models

data class ParentUser(
        val username: String,
        val name: String,
        val family: MutableList<ChildUser>,
        val devices: MutableList<Device>
)

data class ChildUser(
        val username: String,
        val name: String,
        val parent: ParentUser
)