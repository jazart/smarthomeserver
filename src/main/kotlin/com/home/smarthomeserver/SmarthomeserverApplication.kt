package com.home.smarthomeserver

import com.home.smarthomeserver.controllers.AwsBroker
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SmarthomeserverApplication

fun main(args: Array<String>) {

    //val broker = AwsBroker()
    runApplication<SmarthomeserverApplication>(*args)
}