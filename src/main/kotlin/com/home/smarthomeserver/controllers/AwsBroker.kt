package com.home.smarthomeserver.controllers

import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.home.smarthomeserver.devices.LedLight
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.File
import java.lang.annotation.RetentionPolicy
import java.nio.file.Path
import java.nio.file.Paths


@PropertySource("classpath:/application.template.properties")
@Service
class AwsBroker (@Value("\${aws_client_endpoint}") clientEndpoint: String?,
                @Value("\${aws_client_id}") clientId: String?,
                @Value("\${aws_access_key}") awsAccesskey: String? ,
                @Value("\${aws_secret_key}") awsKey: String?) {


    val client: AWSIotMqttClient = AWSIotMqttClient(clientEndpoint, clientId, awsAccesskey, awsKey)

}