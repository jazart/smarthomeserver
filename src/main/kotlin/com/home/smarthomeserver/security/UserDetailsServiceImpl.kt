package com.home.smarthomeserver.security

import com.home.smarthomeserver.repository.ParentUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    lateinit var userRepository: ParentUserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findUserByUsername(username)
        return User.builder().run {
            username(user.username)
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