package com.home.smarthomeserver

import com.amazonaws.services.iot.client.AWSIotMqttClient
import com.amazonaws.services.iot.client.AWSIotQos

class Publisher(val clientId: String, val client: AWSIotMqttClient) {

    fun sendData(data: String) {
        client.publish("/test", AWSIotQos.QOS1, data)
    }

}