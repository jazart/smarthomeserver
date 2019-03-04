package com.home.smarthomeserver.models

import com.home.smarthomeserver.ChildUserRepository
import com.home.smarthomeserver.DeviceRepository
import com.home.smarthomeserver.ParentUserRepository
import com.home.smarthomeserver.entity.ChildUserEntity
import com.home.smarthomeserver.entity.DeviceEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Mapper(){
    @Autowired
    lateinit var userRepository: ParentUserRepository
    @Autowired
    lateinit var userChildRepository: ChildUserRepository
    @Autowired
    lateinit var deviceRepository: DeviceRepository

    fun toUserEnity(currentUser: ParentUser): ParentUserEntity{
        val retrievedUser = userRepository.findUserByUsername(currentUser.username)
        retrievedUser.name = currentUser.name
        val children = currentUser.family.map { childUser ->  toChildUserEntity(childUser)}.toMutableList()
        retrievedUser.family = children
        val devices = currentUser.devices.map { device -> toDeviceEntity(device)}.toMutableList()
        retrievedUser.devices = devices
        return retrievedUser
    }

    fun toChildUserEntity(currentUser: ChildUser): ChildUserEntity {
        val retrievedUser = userChildRepository.findUserByUsername(currentUser.username)
        retrievedUser.name = currentUser.name
        retrievedUser.parent = toUserEnity(currentUser.parent)
        return retrievedUser
    }

    fun toDeviceEntity(device: Device): DeviceEntity {
        val retrievedDevice = deviceRepository.findDeviceEntityById(device.id)
        retrievedDevice?.status = device.status
        retrievedDevice?.commands = device.commands
        return retrievedDevice ?: throw Exception("This should never happen")
    }
}