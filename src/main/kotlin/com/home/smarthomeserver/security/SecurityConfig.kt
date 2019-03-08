package com.home.smarthomeserver.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    override fun userDetailsService(): UserDetailsService = UserDetailsServiceImpl()

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.csrf().disable()
                .authenticationProvider(authenticationProvider())
                .authorizeRequests()
                .antMatchers("/graphql").permitAll()
                .antMatchers("/graphiql").permitAll()
                .anyRequest().authenticated()

        http.apply(JwtTokenFilterConfig(jwtTokenProvider))

    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService())
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }


    @Bean
    fun authManager(): AuthenticationManager = super.authenticationManagerBean()

    @Bean
    fun authenticationProvider():DaoAuthenticationProvider =
            DaoAuthenticationProvider().apply {
                setUserDetailsService(userDetailsService())
                setPasswordEncoder(passwordEncoder())
            }

}