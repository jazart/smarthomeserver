package com.home.smarthomeserver.awsconfig

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.signer.AwsS3V4Signer
import software.amazon.awssdk.auth.signer.params.Aws4PresignerParams
import software.amazon.awssdk.http.SdkHttpFullRequest
import software.amazon.awssdk.http.SdkHttpMethod
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response
import java.net.URI
import java.time.Instant

object AwsS3Client {

    private val client = S3AsyncClient.builder().run {
        credentialsProvider { AwsCredentials.creds }
        region(Region.US_WEST_2)
        build()

    }
    private const val BUCKET_NAME = "smarthome-jazart-images"
    private const val S3 = "s3"
    private const val HOST = "s3.us-west-2.amazonaws.com"
    private const val PROTOCOL = "https"
    private const val PATH_PREFIX = BUCKET_NAME.plus("/")


    fun getSignedUrl(): URI {
        val (sdkReq, params) = buildSdkRequest(getBucketObjects())
        return AwsS3V4Signer.create().presign(sdkReq, params).uri
    }

    private fun buildSdkRequest(bucket: ListObjectsV2Response): Pair<SdkHttpFullRequest, Aws4PresignerParams> {
        val params = Aws4PresignerParams.builder().run {
            expirationTime(Instant.ofEpochSecond(3600L))
            awsCredentials(AwsBasicCredentials.create(AwsCredentials.creds.accessKeyId(),
                    AwsCredentials.creds.secretAccessKey()))
            signingName(S3)
            signingRegion(Region.US_WEST_2)
            build()
        }

        val req = SdkHttpFullRequest.builder().run {
            encodedPath(PATH_PREFIX.plus(bucket.contents().first { obj -> obj.key() == "demo.png" }.key()))
            host(HOST)
            method(SdkHttpMethod.GET)
            protocol(PROTOCOL)
            build()
        }

        return Pair(req, params)
    }

    @Throws(Exception::class)
    private fun getBucketObjects(): ListObjectsV2Response {
        return client.listObjectsV2(ListObjectsV2Request.builder().run {
            bucket(BUCKET_NAME)
            build()
        }).get() ?: throw Exception()
    }

}