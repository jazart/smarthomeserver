package com.home.smarthomeserver.security

import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler

@EnableResourceServer
@Configuration
@Order(2)
class ResourceServerConfig : ResourceServerConfigurerAdapter() {

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources?.resourceId("resource_ids")?.stateless(false)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.anonymous()
                .and()
                .authorizeRequests()
                .antMatchers("/graphql").permitAll()
                .antMatchers("/graphiql").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedHandler(OAuth2AccessDeniedHandler())
    }

}