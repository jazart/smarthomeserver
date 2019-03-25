package com.home.smarthomeserver.awsconfig

import com.amazonaws.services.iot.client.AWSIotMqttClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsBroker {
    private val client: AWSIotMqttClient = AWSIotMqttClient(System.getenv("AWS_CLIENT_ENDPOINT"),
            System.getenv("AWS_CLIENT_ID"),
            System.getenv("AWS_ACCESS_KEY"),
            System.getenv("AWS_SECRET_KEY")
    )

    @Bean(name = ["broker"])
    fun broker(): AWSIotMqttClient = client
}