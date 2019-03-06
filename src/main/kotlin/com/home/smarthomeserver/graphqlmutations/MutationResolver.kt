package com.home.smarthomeserver.graphqlmutations

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.home.smarthomeserver.ChildUserRepository
import com.home.smarthomeserver.SignupException
import com.home.smarthomeserver.UserService
import com.home.smarthomeserver.controllers.DeviceController
import com.home.smarthomeserver.entity.ChildUserEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.models.Command
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
    @Throws(SignupException::class)
    fun signup(username: String, name: String, pass: String): String? {
        val user = ParentUserEntity(name = name, password = encoder.encode(pass),
                username = username, id = 0L)
        return userService.signUp(user)
    }

    @Unsecured
    fun login(name: String, pass: String): String? {
        return userService.login(name, pass)
    }

    fun addChild(username: String, name: String, pass: String, parentName: String): String {
        val user = userService.userRepository.findUserByName(parentName)
        val child = ChildUserEntity(name = name, password = pass, parent = user, id = 0, username = username)
        userService.addChild(user, child)
        return "Ok"
    }

    fun update(uId: String, deviceName: String, command: Command): String {
        controller.connect()
        controller.updateDeviceStatus(uId, deviceName, command)
        return "Ok"
    }


}