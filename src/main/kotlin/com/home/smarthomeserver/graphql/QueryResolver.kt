package com.home.smarthomeserver.graphql

import com.auth0.jwt.exceptions.JWTVerificationException
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.GraphQLResolver
import com.home.smarthomeserver.entity.Status
import com.home.smarthomeserver.models.ChildUser
import com.home.smarthomeserver.models.Device
import com.home.smarthomeserver.models.DeviceType
import com.home.smarthomeserver.models.ParentUser
import com.home.smarthomeserver.repository.ChildUserRepository
import com.home.smarthomeserver.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class DeviceResolver : QueryResolver() {
    fun device(type: DeviceType?) = Device(name = "dev", status = Status.DISCONNECTED, commands = mutableListOf(), owner = "god", type = DeviceType.CAMERA)
}

@Component
class UserDeviceResolver : GraphQLResolver<ParentUser> {
    @Autowired
    lateinit var userService: UserService

    fun devices(user: ParentUser): List<Device> {
        return userService.getUserByName(user.username)?.devices?.toList() ?: emptyList()
    }

}

@Component
class UserFieldResolver : GraphQLResolver<Device> {
    @Autowired
    lateinit var userService: UserService

    fun owner(owner: ParentUser): String {
        return userService.getUserByName(owner.username)?.username ?: ""
    }
}

@Component
class UserResolver : QueryResolver() {
    @Autowired
    lateinit var userService: UserService

    @Throws(JWTVerificationException::class)
    fun user(name: String): ParentUser {
        return userService.getUserByName(name) ?: throw Exception("User not found.")
    }
}

@Component
class ChildUserResolver : QueryResolver() {
    @Autowired
    lateinit var childUserRepository: ChildUserRepository

    fun childUser(username: String): ChildUser? {
        return null
    }
}

@Component
class QueryResolver : GraphQLQueryResolver