package com.home.smarthomeserver.controllers

import com.amazonaws.services.iot.client.AWSIotMqttClient
import org.springframework.stereotype.Component


@Component
class AwsBroker {
    val client: AWSIotMqttClient = AWSIotMqttClient(System.getenv("AWS_CLIENT_ENDPOINT"),
            System.getenv("AWS_CLIENT_ID"),
            System.getenv("AWS_ACCESS_KEY"),
            System.getenv("AWS_SECRET_KEY")
    )



}