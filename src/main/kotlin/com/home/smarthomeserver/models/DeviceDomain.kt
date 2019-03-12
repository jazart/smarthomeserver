package com.home.smarthomeserver.models

import com.home.smarthomeserver.entity.Status
import javax.persistence.*

data class Device  (
        val name: String,

        @Enumerated(EnumType.STRING)
        val status: Status,

        @ElementCollection
        val commands: MutableList<Command>,

        val id: Long,

        val owner: ParentUser = ParentUser()

)

data class Light (
        val name: String,

        @ElementCollection
        val commands: MutableList<Command>,

        val id: Long

)
