package com.home.smarthomeserver.graphqlqueries

import com.auth0.jwt.exceptions.JWTVerificationException
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.home.smarthomeserver.ChildUserRepository
import com.home.smarthomeserver.UserService
import com.home.smarthomeserver.entity.Status
import com.home.smarthomeserver.models.ChildUser
import com.home.smarthomeserver.models.Device
import com.home.smarthomeserver.models.DeviceType
import com.home.smarthomeserver.models.ParentUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class DeviceResolver : QueryResolver() {
    fun device(type: DeviceType?) = Device(name = "dev", id = 0, status = Status.DISCONNECTED, commands = mutableListOf())
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