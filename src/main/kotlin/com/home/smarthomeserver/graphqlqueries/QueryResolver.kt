package com.home.smarthomeserver.graphqlqueries

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.home.smarthomeserver.ParentUserRepository
import com.home.smarthomeserver.entity.Device
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class DeviceResolver : QueryResolver() {
    fun device() = Device()
}

@Component
class UserResolver : QueryResolver() {
    fun user(name: String) = userRepository.findUserByName(name)
}

@Component
abstract class QueryResolver : GraphQLQueryResolver {
    @Autowired
    lateinit var userRepository: ParentUserRepository
}