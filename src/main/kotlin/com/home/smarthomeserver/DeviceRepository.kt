package com.home.smarthomeserver

import com.home.smarthomeserver.entity.DeviceEntity
import com.home.smarthomeserver.entity.LightEntity
import com.home.smarthomeserver.models.Device
import com.home.smarthomeserver.models.Light
import org.springframework.data.jpa.repository.JpaRepository

interface DeviceRepository<T : DeviceEntity> : JpaRepository<T,String>{
    fun findDeviceByID(id: Long): DeviceEntity?
    fun existsDeviceByID(id: String): Boolean
}

interface LightRepository<LightEntity> : JpaRepository<com.home.smarthomeserver.entity.LightEntity,String>{
    fun findDeviceByID(id: Long): Light?
    fun existsDeviceByID(id: String): Boolean
}