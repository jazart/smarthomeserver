//package com.home.smarthomeserver.security
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.autoconfigure.web.ServerProperties
//import org.springframework.boot.jdbc.DataSourceBuilder
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.core.io.Resource
//import org.springframework.jdbc.datasource.init.DataSourceInitializer
//import org.springframework.jdbc.datasource.init.DatabasePopulator
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
//import org.springframework.security.oauth2.provider.ClientDetailsService
//import org.springframework.security.oauth2.provider.token.TokenStore
//import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
//import javax.sql.DataSource
//
//
//@Configuration
//@EnableAuthorizationServer
//class AuthServerConfig : AuthorizationServerConfigurerAdapter() {
//
//    @Value("classpath:schema.sql")
//    lateinit var schemaScript: Resource
//
//    @Autowired
//    lateinit var clientDetailsService: ClientDetailsService
//
//    @Autowired
//    lateinit var encryptor: BCryptPasswordEncoder
//
//    @Autowired
//    lateinit var authManager: AuthenticationManager
//
//
//    override fun configure(server: AuthorizationServerSecurityConfigurer?) {
//        server?.run {
//            tokenKeyAccess("permitAll()")
//            checkTokenAccess("isAuthenticated()")
//        }
//    }
//
//    override fun configure(clients: ClientDetailsServiceConfigurer?) {
//        clients?.run {
//            jdbc(dataSource())
//        }
////        clients?.inMemory()
////                ?.withClient("TestId")
////                ?.authorizedGrantTypes("authorization_code", "refresh_token", "password")
////                ?.refreshTokenValiditySeconds(5_000_000)
////                ?.accessTokenValiditySeconds(500)
////                ?.authorities("USER")
////                ?.scopes("read", "write")
////                ?.autoApprove(true)
////                ?.secret(encryptor.encode("secret"))
//
//
//    }
//
//    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
//        endpoints?.apply {
//            authenticationManager(authManager)
//            tokenStore(tokenStore())
//        }
//    }
//
//    @Bean
//    fun tokenStore(): TokenStore = JdbcTokenStore(dataSource())
//
//    @Bean
//    fun dataSource(): DataSource =
//            DataSourceBuilder.create().run {
//                driverClassName("")
//                username("root")
//                password("pass")
//                url("")
//                build()
//            }
//
//    @Bean
//    fun dataSourceInitializer(dataSource: DataSource): DataSourceInitializer =
//            DataSourceInitializer().apply {
//                setDataSource(dataSource)
//                setDatabasePopulator(databasePopulator())
//            }
//
//    private fun databasePopulator(): DatabasePopulator {
//        val populator = ResourceDatabasePopulator()
//        populator.addScript(schemaScript)
//        return populator
//    }
//}
