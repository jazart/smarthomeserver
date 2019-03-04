package com.home.smarthomeserver.security

import com.home.smarthomeserver.ParentUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    lateinit var userRepository: ParentUserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findUserByName(username)
        return User.builder().run {
            username(user.name)
            password(user.password)
            accountExpired(false)
            accountLocked(false)
            disabled(false)
            credentialsExpired(false)
            authorities(listOf())
            build()
        }
    }

}