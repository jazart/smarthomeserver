package com.home.smarthomeserver.awsconfig

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.iot.IotAsyncClient

object AwsIotClient {

    private val client: IotAsyncClient = IotAsyncClient.builder().run {
        region(Region.US_WEST_2)
        credentialsProvider {
            AwsBasicCredentials.create(
                    System.getenv("AWS_ACCESS_KEY"),
                    System.getenv("AWS_SECRET_KEY")
            )
        }
        build()
    }

    @Synchronized fun get() = client
}