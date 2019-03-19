package com.home.smarthomeserver

import com.home.smarthomeserver.entity.DeviceEntity
import com.home.smarthomeserver.entity.LightEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DeviceRepository : JpaRepository<DeviceEntity, String> {
    fun findDeviceEntityById(id: Long): DeviceEntity?
    fun existsDeviceEntityById(id: Long): Boolean
    fun existsDeviceEntityByNameAndOwnerUsername(name: String, ownerUsername: String): Boolean
}


interface LightRepository : JpaRepository<LightEntity, String> {
    fun findLightEntityById(id: Long): LightEntity
}