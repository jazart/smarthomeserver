package com.home.smarthomeserver.graphqlmutations

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.home.smarthomeserver.ChildUserRepository
import com.home.smarthomeserver.DeviceService
import com.home.smarthomeserver.SignupException
import com.home.smarthomeserver.UserService
import com.home.smarthomeserver.entity.ChildUserEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.models.*
import com.home.smarthomeserver.security.Unsecured
import graphql.GraphQLException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component


@Component
class MutationResolver : GraphQLMutationResolver {

    @Autowired
    lateinit var deviceService: DeviceService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var childUserRepository: ChildUserRepository

    @Unsecured
    @Throws(SignupException::class)
    fun signup(creds: Credential, info: Personal): String? {
        val user = ParentUserEntity(firstName = info.firstName,
                lastName = info.lastName,
                password = creds.password,
                username = creds.username, id = 0L, email = info.email)
        return userService.signUp(user)
    }

    @Unsecured
    @Throws(SignupException::class)
    fun login(name: String, pass: String): String? {
        return userService.login(name, pass)
    }

    fun addChild(username: String, firstName: String, lastName: String, pass: String, parentName: String, email: String): String {
        val user = userService.userRepository.findUserByUsername(parentName)
        val child = ChildUserEntity(firstName = firstName, lastName = lastName, password = pass, parent = user, id = 0, username = username, email = email)
        userService.addChild(user, child)
        return "Ok"
    }

    @Unsecured
    fun update(uId: String, deviceName: String, command: Command): String {
        deviceService.run {
            connect()
            updateDeviceStatus(uId, deviceName, command)
        }
        return "Ok"
    }

    @Throws(GraphQLException::class)
    fun addDevice(username: ParentUser, deviceName: String, device: Device) {

    }

    @Unsecured
    @Throws(GraphQLException::class)
    fun removeDevice(username: String, dID: String, deviceName: String){
        deviceService.removeDevice(dID)
    }

    fun modifyDeviceName(dId: String, deviceName: String){
        deviceService.modifyDeviceName(dId,deviceName)
    }
}