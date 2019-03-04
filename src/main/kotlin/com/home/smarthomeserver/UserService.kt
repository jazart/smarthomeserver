package com.home.smarthomeserver

import com.home.smarthomeserver.entity.ChildUserEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.entity.toUserDomain
import com.home.smarthomeserver.models.ParentUser
import com.home.smarthomeserver.security.JwtTokenProvider
import com.home.smarthomeserver.security.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var encryptor: BCryptPasswordEncoder

    @Autowired
    lateinit var userRepository: ParentUserRepository

    @Autowired
    lateinit var childUserRepository: ChildUserRepository

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var userDetailsServiceImpl: UserDetailsServiceImpl


    @Throws(Exception::class)
    fun login(name: String, password: String): String {
        if (userRepository.existsUserByName(name)) {
            val user = userDetailsServiceImpl.loadUserByUsername(name)
            if (isValid(user, password)) {
                return jwtTokenProvider.createToken(name)
            }
            throw Exception("Invalid username/password")
        } else {
            throw Exception("Invalid username/password")
        }
        
    }

    fun signUp(user: ParentUserEntity): String {
        if (!userRepository.existsUserByName(user.name)) {
            userRepository.save(user)
            return jwtTokenProvider.createToken(user.name)
        }
        return ""
    }

    fun addChild(parent: ParentUserEntity, child: ChildUserEntity) {
        childUserRepository.save(child)
        parent.family.add(child)
        userRepository.save(parent)
    }

    fun getUserByName(username: String): ParentUser? = userRepository.findUserByName(username).toUserDomain()

    fun isValid(user: UserDetails, password: String): Boolean =
            user.password == encryptor.encode(password) && user.isAccountNonExpired
                    && user.isAccountNonLocked && user.isCredentialsNonExpired

}