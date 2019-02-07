package com.home.smarthomeserver.controllers

import com.amazonaws.services.iot.client.*
import com.amazonaws.services.iot.client.AWSIotMqttClient



class AwsBroker {



    init {
        var clientEndpoint = "a81jmhvfdxdf-ats.iot.us-west-2.amazonaws.com"       // replace <prefix> and <region> with your own
        var clientId = "test"                              // replace with your own client ID. Use unique client IDs for concurrent connections.
        var awsAccesskey = "AKIAJZN5LIYOCOCFBIVQ"                       // X.509 based certificate file
        var awsKey = "IdOmetPko51kjgYBw2ur0zbbzxg7kZPcUfB0EMGu"                        //
        var client = AWSIotMqttClient(clientEndpoint, clientId, awsAccesskey, awsKey)
        //client.connect()
        try{

            client.connect()
            println("connection Sucessful ========================================success")
            client.disconnect()
        }catch(E: AWSIotException){
            throw IllegalArgumentException("Failed to connect to AWS IoT Client.")
        }

        /* PKCS#1 or PKCS#8 PEM encoded private key file

        // SampleUtil.java and its dependency PrivateKeyReader.java can be copied from the sample source code.
        // Alternatively, you could load key store directly from a file - see the example included in this README.
        var pair = SampleUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile)
        var client = AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword)*/
    }
}