package com.home.smarthomeserver

import com.home.smarthomeserver.entity.DeviceEntity
import com.home.smarthomeserver.entity.LightEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DeviceRepository : JpaRepository<DeviceEntity, String> {
    fun findDeviceEntityById(id: Long): DeviceEntity?
    fun existsDeviceEntityById(id: String): Boolean
}


interface LightRepository : JpaRepository<LightEntity, String> {
    fun findLightEntityById(id: String): LightEntity
}