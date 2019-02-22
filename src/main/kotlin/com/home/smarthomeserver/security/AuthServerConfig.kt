package com.home.smarthomeserver.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore

@Configuration
@EnableAuthorizationServer
class AuthServerConfig : AuthorizationServerConfigurerAdapter() {

    @Autowired
    lateinit var encryptor: BCryptPasswordEncoder

    @Autowired
    lateinit var tokenStore: TokenStore

    @Autowired
    lateinit var authManager: AuthenticationManager

    override fun configure(server: AuthorizationServerSecurityConfigurer?) {
        server?.run {
            tokenKeyAccess("permitAll()")
            checkTokenAccess("isAuthenticated()")
        }
    }

    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients?.inMemory()
                ?.withClient("TestId")
                ?.authorizedGrantTypes("authorization_code", "refresh_token", "password")
                ?.refreshTokenValiditySeconds(5_000_000)
                ?.accessTokenValiditySeconds(500)
                ?.authorities("USER")
                ?.scopes("read", "write")
                ?.autoApprove(true)
                ?.secret(encryptor.encode("secret"))


    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints
                ?.authenticationManager(authManager)
                ?.tokenStore(tokenStore)
    }

    @Bean
    fun tokenStore(): TokenStore = InMemoryTokenStore()
}
