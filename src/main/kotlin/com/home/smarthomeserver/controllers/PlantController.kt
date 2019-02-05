package com.home.smarthomeserver.controllers

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.home.smarthomeserver.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class PlantController {
    @RequestMapping("/test", method = [RequestMethod.GET])
    fun plant()= "Works!"
}

class DeviceResolver : GraphQLQueryResolver {
    fun device() = Device()
}

class UserResolver : GraphQLQueryResolver {
    fun user() = User()
}

class DeviceInformationResolver : GraphQLQueryResolver {
    fun deviceInfo() = DeviceInformation()
}

@Configuration
class GraphQLConfiguration {

    @Bean
    fun deviceResolver() = DeviceResolver()

    @Bean
    fun userResolver() = UserResolver()

    @Bean
    fun deviceInfoResolver() = DeviceInformationResolver()
}
