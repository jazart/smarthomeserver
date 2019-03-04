package com.home.smarthomeserver.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import javax.sql.DataSource


@Configuration
class DatasourceConfig {

    @Value("classpath:schema.sql")
    lateinit var schemaScript: Resource

    @Value("classpath:data.sql")
    lateinit var data: Resource

    @Bean
    fun dataSource(): DataSource =
            DataSourceBuilder.create().run {
                driverClassName("org.postgresql.Driver")
                username(System.getenv("RDS_USERNAME"))
                password(System.getenv("RDS_PASSWORD"))
                url("jdbc:postgresql://".plus(System.getenv("RDS_HOSTNAME")).plus(":5432/postgres"))
                build()
            }

    @Bean
    fun dataSourceInitializer(dataSource: DataSource): DataSourceInitializer =
            DataSourceInitializer().apply {
                setDataSource(dataSource)
            }
}

