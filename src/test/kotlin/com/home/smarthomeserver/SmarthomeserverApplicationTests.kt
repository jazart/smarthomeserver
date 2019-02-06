/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.home.smarthomeserver

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

import org.assertj.core.api.BDDAssertions.then

/**
 * Basic integration tests for service demo application.
 *
 * @author Dave Syer
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["management.port=0"])
class HelloWorldApplicationTests {

    @LocalServerPort
    private val port: Int = 0

    @Value("\${local.management.port}")
    private val mgt: Int = 0

    @Autowired
    private val testRestTemplate: TestRestTemplate? = null

    @Test
    @Throws(Exception::class)
    fun shouldReturn200WhenSendingRequestToController() {
        val entity = this.testRestTemplate?.getForEntity(
                "http://localhost:" + this.port + "/person", Map::class.java)

        then(entity?.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturn200WhenSendingRequestToManagementEndpoint() {
        val entity = this.testRestTemplate?.getForEntity(
                "http://localhost:" + this.mgt + "/actuator/info", Map::class.java)

        then(entity?.statusCode).isEqualTo(HttpStatus.OK)
    }

}