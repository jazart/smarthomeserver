package com.home.smarthomeserver.models

import com.home.smarthomeserver.entity.Device
import com.home.smarthomeserver.entity.Light
import com.home.smarthomeserver.entity.Status
import javax.persistence.*

data class DeviceView  (
        val name: String,

        @Enumerated(EnumType.STRING)
        val status: Status,

        @ElementCollection
        val commands: MutableList<Command>

)

data class LightView  (
        val name: String,

        @ElementCollection
        val commands: MutableList<Command>

)
