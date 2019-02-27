package com.home.smarthomeserver.graphqlmutations

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.home.smarthomeserver.ChildUserRepository
import com.home.smarthomeserver.UserService
import com.home.smarthomeserver.controllers.DeviceController
import com.home.smarthomeserver.models.ChildUser
import com.home.smarthomeserver.models.Command
import com.home.smarthomeserver.models.ParentUser
import com.home.smarthomeserver.security.Unsecured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component


@Component
class MutationResolver : GraphQLMutationResolver {

    @Autowired
    lateinit var controller: DeviceController

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var encoder: BCryptPasswordEncoder

    @Autowired
    lateinit var childUserRepository: ChildUserRepository

    @Unsecured
    fun signup(name: String, pass: String): String? {
        val user = ParentUser(name = name, password = encoder.encode(pass))
        return userService.signUp(user)
    }

    @Unsecured
    fun login(name: String, pass: String): String? {
        return userService.login(name,pass)
    }

    fun addChild(name: String, pass: String, parentName: String): String {
        val user = userService.userRepository.findUserByName(parentName)
        val child = ChildUser(name = name, password = pass, parent = user)
        userService.addChild(user, child)
        return "Ok"
    }

    fun update(uId: String, deviceName: String, command: Command): String {
        controller.connect()
        controller.updateDeviceStatus(uId, deviceName, command)
        return "Ok"
    }
}