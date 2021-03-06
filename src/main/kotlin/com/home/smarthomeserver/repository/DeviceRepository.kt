package com.home.smarthomeserver.repository

import com.home.smarthomeserver.entity.DeviceEntity
import com.home.smarthomeserver.entity.LightEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DeviceRepository : JpaRepository<DeviceEntity, String> {
    fun findDeviceEntityById(id: Long): DeviceEntity?
    fun findDeviceEntityByNameAndOwnerUsername(deviceName: String, username: String): DeviceEntity?
    fun existsDeviceEntityById(id: Long): Boolean
    fun existsDeviceEntityByNameAndOwnerUsername(name: String, ownerUsername: String): Boolean
    fun findDeviceEntityByFavoriteTrueAndOwnerUsername(username: String): DeviceEntity?
    fun findDeviceEntityByNameAndOwnerUsernameAndFavoriteTrue(deviceName: String, username: String): DeviceEntity?
}


interface LightRepository : JpaRepository<LightEntity, String> {
    fun findLightEntityById(id: Long): LightEntity
}