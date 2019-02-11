package com.home.smarthomeserver.graphqlqueries

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.home.smarthomeserver.Device
import com.home.smarthomeserver.DeviceInformation
import com.home.smarthomeserver.User

class DeviceResolver : GraphQLQueryResolver {
    fun device() = Device()
}

class UserResolver : GraphQLQueryResolver {
    fun user() = User()
}

class DeviceInformationResolver : GraphQLQueryResolver {

    fun deviceInfo() = DeviceInformation()
}
