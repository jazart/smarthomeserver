package com.home.smarthomeserver.models

import com.home.smarthomeserver.repository.ChildUserRepository
import com.home.smarthomeserver.repository.DeviceRepository
import com.home.smarthomeserver.repository.ParentUserRepository
import com.home.smarthomeserver.entity.ChildUserEntity
import com.home.smarthomeserver.entity.DeviceEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Mapper {
    @Autowired
    lateinit var userRepository: ParentUserRepository
    @Autowired
    lateinit var userChildRepository: ChildUserRepository
    @Autowired
    lateinit var deviceRepository: DeviceRepository

    fun toParentEntity(currentUser: ParentUser): ParentUserEntity{
        val retrievedUser = userRepository.findUserByUsername(currentUser.username)
        val children = currentUser.family.map { childUser ->  toChildUserEntity(childUser)}.toMutableList()
        retrievedUser.family = children
        val devices = currentUser.devices.map { device -> toDeviceEntity(device)}.toMutableList()
        retrievedUser.devices = devices
        return retrievedUser
    }

    fun toChildUserEntity(currentUser: ChildUser): ChildUserEntity {
        val retrievedUser = userChildRepository.findUserByUsername(currentUser.username)
        retrievedUser.parent = toParentEntity(currentUser.parent)
        return retrievedUser
    }

    fun toDeviceEntity(device: Device): DeviceEntity {
        val retrievedDevice = deviceRepository.findDeviceEntityById(device.id)
        retrievedDevice?.status = device.status
        retrievedDevice?.commands = device.commands
        return retrievedDevice ?: throw Exception("This should never happen")
    }

}