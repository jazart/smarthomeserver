package com.home.smarthomeserver

import com.home.smarthomeserver.service.DeviceService
import com.home.smarthomeserver.service.UserService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@TestConfiguration
class TestConfig {
    @Bean
    fun deviceService() = DeviceService()

    @Bean
    fun userService() = UserService()

    @Bean
    fun encoder() = BCryptPasswordEncoder()
}