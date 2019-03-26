package com.home.smarthomeserver.models

import com.home.smarthomeserver.entity.Status

data class Device  (
        val name: String,

        val status: Status,

        val commands: MutableList<Command>,

        val owner: String

)

data class Light (
        val name: String,

        val commands: MutableList<Command>
)
