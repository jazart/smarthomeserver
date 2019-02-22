package com.home.smarthomeserver.graphqlqueries

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.home.smarthomeserver.model.User
import com.home.smarthomeserver.models.Device
import com.home.smarthomeserver.models.DeviceInformation
import com.home.smarthomeserver.security.Unsecured
import org.springframework.stereotype.Component

@Component
class DeviceResolver : GraphQLQueryResolver {
    fun device() = Device()
}

@Component
class UserResolver : GraphQLQueryResolver {
    fun user() = User()
}

@Component
class DeviceInformationResolver : GraphQLQueryResolver {
    fun deviceInfo() = DeviceInformation()
}