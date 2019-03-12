package com.home.smarthomeserver.models

data class ParentUser(
        val username: String,
        val firstName: String,
        val lastName: String,
        val family: MutableList<ChildUser> = mutableListOf(),
        val devices: MutableList<Device> = mutableListOf()
) {
    constructor() : this(username = "",
            firstName = "",
            lastName = "")
}

data class ChildUser(
        val username: String,
        val firstName: String,
        val lastName: String,
        val parent: ParentUser
)