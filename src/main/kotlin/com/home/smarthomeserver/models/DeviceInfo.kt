package com.home.smarthomeserver.models

data class DeviceInfo(val username: String, val deviceName: String, val command: MutableList<Command>, val isFavorite: Boolean, val type: DeviceType)