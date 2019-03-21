package com.home.smarthomeserver

import org.springframework.beans.factory.config.MethodInvokingFactoryBean
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import javax.sql.DataSource




@Configuration
class DatasourceConfig {

    @Bean
    fun dataSource(): DataSource =
            DataSourceBuilder.create().run {
                driverClassName("org.postgresql.Driver")
                username("postgres")
                password("root")
                url("jdbc:postgresql://localhost:5432/postgres")
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

