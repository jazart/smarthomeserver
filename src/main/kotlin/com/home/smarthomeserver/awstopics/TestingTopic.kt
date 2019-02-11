package com.home.smarthomeserver.awstopics

import com.amazonaws.services.iot.client.AWSIotMessage
import com.amazonaws.services.iot.client.AWSIotQos
import com.amazonaws.services.iot.client.AWSIotTopic

class TestingTopic(topic: String, qos: AWSIotQos) : AWSIotTopic(topic, qos) {
    override fun onMessage(message: AWSIotMessage?) {
        super.onMessage(message)
        println(message?.stringPayload)
    }
}