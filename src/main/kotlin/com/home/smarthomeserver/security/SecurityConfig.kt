package com.home.smarthomeserver.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
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
                .authorizeRequests()
                .antMatchers("/graphql").permitAll()
                .antMatchers("/graphiql").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()

        http.apply(JwtTokenFilterConfig(jwtTokenProvider))
    }

//    override fun configure(auth: AuthenticationManagerBuilder?) {
//        auth?.userDetailsService(userDetailsService())?.passwordEncoder(passwordEncoder())
//    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authManager(): AuthenticationManager = super.authenticationManagerBean()
}