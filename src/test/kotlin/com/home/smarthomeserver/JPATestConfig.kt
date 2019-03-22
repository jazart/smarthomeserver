//package com.home.smarthomeserver
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.TestConfiguration
//import org.springframework.context.annotation.Bean
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories
//import org.springframework.jdbc.datasource.DriverManagerDataSource
//import javax.sql.DataSource
//
//@TestConfiguration
//@EnableJpaRepositories(basePackageClasses = [DeviceRepository::class])
//class JPATestConfig {
//
//    @Autowired
//    lateinit var env: org.springframework.core.env.Environment
//
//    @Bean
//    fun dataSource(): DataSource {
//        val dataSource = DriverManagerDataSource()
//        with(dataSource) {
//            setDriverClassName(env.getProperty("jdbc.driverClassName", ""))
//            url = env.getProperty("jdbc.url")
//            username = env.getProperty("jdbc.user")
//            password = env.getProperty("jdbc.pass")
//        }
//        return dataSource
//    }
//}