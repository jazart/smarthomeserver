package com.home.smarthomeserver

import com.amazonaws.services.iot.client.AWSIotMessage
import com.amazonaws.services.iot.client.AWSIotQos
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class TestMessage : AWSIotMessage(null, AWSIotQos.QOS0) {
    override fun onSuccess() {
        super.onSuccess()
        println("Heres the doc: $stringPayload")
    }

    override fun onFailure() {
        super.onFailure()
        println("message failed ---------> FAILED")
    }

    override fun onTimeout() {
        super.onTimeout()
    }

    override fun setStringPayload(payload: String?) {
    }
}
