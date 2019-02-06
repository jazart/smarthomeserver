package com.home.smarthomeserver.controllers

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.home.smarthomeserver.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class PlantController {

    @Autowired lateinit var broker: AwsBroker

    @RequestMapping("/plant/{id}", method = [RequestMethod.GET])
    fun sendPlantStatus(@PathVariable("id") id: String) {
        PlantMoisture()
    }
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
