package com.home.smarthomeserver.graphqlqueries

import com.auth0.jwt.exceptions.JWTVerificationException
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.home.smarthomeserver.UserService
import com.home.smarthomeserver.entity.Status
import com.home.smarthomeserver.models.Device
import com.home.smarthomeserver.models.ParentUser
import org.springframework.stereotype.Component


@Component
class DeviceResolver : QueryResolver() {
    fun device() = Device(name = "dev", id = 0, status = Status.DISCONNECTED, commands = mutableListOf())
}

@Component
class UserResolver(val userService: UserService) : QueryResolver() {


    @Throws(JWTVerificationException::class)
    fun user(name: String): ParentUser {
        return userService.getUserByName(name) ?: throw Exception("User not found.")
    }
}

@Component
abstract class QueryResolver : GraphQLQueryResolver