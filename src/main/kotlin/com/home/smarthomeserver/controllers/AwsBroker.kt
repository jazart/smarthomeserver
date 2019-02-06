package com.home.smarthomeserver.controllers

import com.amazonaws.services.iot.client.AWSIotMqttClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component


@PropertySource("file:/C:/Users/jazart/smarthomeserver/config/application.template.properties")
@Component
class AwsBroker (@Value("\${aws_client_endpoint}") clientEndpoint: String?,
                @Value("\${aws_client_id}") clientId: String?,
                @Value("\${aws_access_key}") awsAccesskey: String? ,
                @Value("\${aws_secret_key}") awsKey: String?) {


    val client: AWSIotMqttClient = AWSIotMqttClient(clientEndpoint, clientId, awsAccesskey, awsKey)

}