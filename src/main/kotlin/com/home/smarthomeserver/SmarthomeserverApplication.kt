package com.home.smarthomeserver

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class SmarthomeserverApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
    runApplication<SmarthomeserverApplication>(*args)
}