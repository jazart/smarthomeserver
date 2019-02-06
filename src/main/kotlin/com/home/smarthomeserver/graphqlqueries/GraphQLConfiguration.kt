package com.home.smarthomeserver.graphqlqueries

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GraphQLConfiguration {

    @Bean
    fun deviceResolver() = DeviceResolver()

    @Bean
    fun userResolver() = UserResolver()

    @Bean
    fun deviceInfoResolver() = DeviceInformationResolver()

}