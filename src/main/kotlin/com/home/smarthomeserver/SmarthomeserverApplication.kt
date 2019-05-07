package com.home.smarthomeserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy
class SmarthomeserverApplication : SpringBootServletInitializer()
/** Application Entry Point **/
fun main(args: Array<String>) {
    runApplication<SmarthomeserverApplication>(*args)
}

