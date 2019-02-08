package com.home.smarthomeserver.controllers

import com.amazonaws.services.iot.client.AWSIotMessage
import com.amazonaws.services.iot.client.AWSIotQos
import com.amazonaws.services.iot.client.AWSIotTopic
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.home.smarthomeserver.*
import com.home.smarthomeserver.AWSTopics.TestingTopic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class DeviceController {

    @Autowired lateinit var broker: AwsBroker

    @RequestMapping("/device/{id}", method = [RequestMethod.GET])
    fun sendPlantStatus(@PathVariable("id") id: String) {
        if(broker.client.connection.isUserDisconnect) {
            broker.client.connect()
            val topic = TestingTopic("/testing", AWSIotQos.QOS0)
            broker.client.subscribe(topic, 5000)
            broker.client.publish(AWSIotMessage("testing", AWSIotQos.QOS0, "Spring"))
        } else {
            println("==========connected=================")
        }
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
