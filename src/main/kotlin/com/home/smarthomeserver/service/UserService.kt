package com.home.smarthomeserver.service

import com.home.smarthomeserver.repository.ChildUserRepository
import com.home.smarthomeserver.repository.ParentUserRepository
import com.home.smarthomeserver.SignupException
import com.home.smarthomeserver.entity.ChildUserEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.entity.toUserDomain
import com.home.smarthomeserver.models.ParentUser
import com.home.smarthomeserver.security.JwtTokenProvider
import com.home.smarthomeserver.security.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class UserService {

    @Autowired
    lateinit var passwordEncoder: BCryptPasswordEncoder

    @Autowired
    lateinit var userRepository: ParentUserRepository

    @Autowired
    lateinit var childUserRepository: ChildUserRepository

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    lateinit var userDetailsServiceImpl: UserDetailsServiceImpl


    @Throws(SignupException::class)
    fun login(name: String, password: String): String {
        if (userRepository.existsParentUserEntityByUsername(name)) {
            val user = userDetailsServiceImpl.loadUserByUsername(name)
            if (isValid(user, password)) {
                return jwtTokenProvider.createToken(name)
            }
            throw SignupException("Invalid username/password")
        } else {
            throw SignupException("Invalid username/password")
        }
    }

    @Throws(SignupException::class)
    fun signUp(user: ParentUserEntity): String {
        isPasswordValid(user.password)
        user.password = passwordEncoder.encode(user.password)
        when {
            userRepository.existsParentUserEntityByUsername(user.username) -> throw SignupException("User already signed up")
            userRepository.existsParentUserEntityByEmail(user.email) -> throw SignupException("Email is taken")
            else -> {
                userRepository.save(user)
                return jwtTokenProvider.createToken(user.username)
            }
        }
    }

    fun addChild(parent: ParentUserEntity, child: ChildUserEntity) {
        parent.family.add(child)
        userRepository.save(parent)
    }


    @Transactional(propagation = Propagation.REQUIRED)
    fun getUserByName(username: String): ParentUser? = userRepository.findUserByUsername(username).toUserDomain()

    @Transactional(propagation = Propagation.REQUIRED)
    fun getUserEntity(username: String, fetchEager: Boolean = true): ParentUserEntity {
        val parent = userRepository.findUserByUsername(username)
        if(fetchEager) {
            val size = parent.devices.size
        }
        return parent
    }
    fun isValid(user: UserDetails, password: String): Boolean =
            (passwordEncoder.matches(password, user.password) && user.isAccountNonExpired
                    && user.isAccountNonLocked && user.isCredentialsNonExpired)

    @Throws(SignupException::class)
    fun isPasswordValid(password: String): Boolean {
        PasswordValidator.isValid(password)?.let { throw SignupException(it.joinToString("\n")) }
        return true
    }
}