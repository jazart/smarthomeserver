package com.home.smarthomeserver

import org.springframework.beans.factory.config.MethodInvokingFactoryBean
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import javax.sql.DataSource




@Configuration
class DatasourceConfig {

    /**
     * Creates the data source configuration by passing the db driver, name, hosted url, endpoint, and password. These
     * are pulled from systel level env variables. You will need to enter your datasource info here or update your system
     * env properties
     */
    @Bean
    fun dataSource(): DataSource =
            DataSourceBuilder.create().run {
                driverClassName("org.postgresql.Driver")
                username(System.getenv("RDS_USERNAME"))
                password(System.getenv("RDS_PASSWORD"))
                url("jdbc:postgresql://" + System.getenv("RDS_HOSTNAME") + ":5432/postgres")
                build()
            }

    @Bean
    fun methodInvokingFactoryBean(): MethodInvokingFactoryBean {
        val methodInvokingFactoryBean = MethodInvokingFactoryBean()
        methodInvokingFactoryBean.targetClass = SecurityContextHolder::class.java
        methodInvokingFactoryBean.targetMethod = "setStrategyName"
        methodInvokingFactoryBean.setArguments((SecurityContextHolder.MODE_INHERITABLETHREADLOCAL))
        return methodInvokingFactoryBean
    }
}

