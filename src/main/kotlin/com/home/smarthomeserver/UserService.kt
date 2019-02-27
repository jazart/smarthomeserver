package com.home.smarthomeserver

import com.home.smarthomeserver.models.ChildUser
import com.home.smarthomeserver.models.ParentUser
import com.home.smarthomeserver.security.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: ParentUserRepository

    @Autowired
    lateinit var childUserRepository: ChildUserRepository

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    lateinit var authenticationManager: AuthenticationManager


    @Throws(Exception::class)
    fun signIn(name: String, password: String): String {
        if (userRepository.existsUserByName(name)){
            try {
                authenticationManager.authenticate(UsernamePasswordAuthenticationToken(name, password))
                return jwtTokenProvider.createToken(name)
            } catch (e: AuthenticationException) {
                throw Exception("Invalid username/password.")
            }
        }
        else{
            throw Exception("User does not exist")
        }
    }

    fun signUp(user: ParentUser): String {
        if (!userRepository.existsUserByName(user.name)) {
            userRepository.save(user)
            return jwtTokenProvider.createToken(user.name)
        }
        return ""
    }

    fun addChild(parent: ParentUser, child: ChildUser) {
        childUserRepository.save(child)
        parent.family.add(child)
        userRepository.save(parent)
    }
}