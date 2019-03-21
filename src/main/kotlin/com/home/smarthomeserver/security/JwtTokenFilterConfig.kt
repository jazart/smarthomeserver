package com.home.smarthomeserver.security

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtTokenFilterConfig(private val jwtTokenProvider: JwtTokenProvider)
    : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(builder: HttpSecurity?) {
//        builder?.addFilterAfter(JWTAuthorizationFilter(jwtTokenProvider), SecurityContextPersistenceFilter::class.java)
        builder?.addFilterBefore(JWTAuthorizationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java)
    }
}