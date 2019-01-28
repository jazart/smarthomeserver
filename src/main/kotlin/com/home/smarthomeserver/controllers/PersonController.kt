package com.home.smarthomeserver.controllers

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.home.smarthomeserver.Device
import com.home.smarthomeserver.User
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController class PersonController {

    @RequestMapping("/person", method = [RequestMethod.GET])
    fun person(@RequestParam(value="name", defaultValue = "Payne") name: String) =
            User("Jeremy".plus(" ").plus(name), "123", "Tired")

}

class Query : GraphQLQueryResolver {
    fun person() = User("Jeremy", UUID.randomUUID().toString(), "Litty")
}

class DeviceResolver : GraphQLQueryResolver {
    fun device() = Device()
}
@Configuration class GraphQLConfiguration {
    @Bean
    fun queryResolver() = Query()

    @Bean
    fun deviceResolver() = DeviceResolver()
}