package com.home.smarthomeserver

import com.home.smarthomeserver.entity.ChildUserEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.entity.toUserDomain
import com.home.smarthomeserver.models.ParentUser
import com.home.smarthomeserver.security.JwtTokenProvider
import com.home.smarthomeserver.security.UserDetailsServiceImpl
import org.hibernate.validator.cfg.defs.EmailDef
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

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
        if (!userRepository.existsParentUserEntityByUsername(user.username)) {
            userRepository.save(user)
            return jwtTokenProvider.createToken(user.username)
        }

        val mail = JavaMailSenderImpl()
        throw SignupException("User already signed up")
    }

    fun addChild(parent: ParentUserEntity, child: ChildUserEntity) {
        childUserRepository.save(child)
        parent.family.add(child)
        userRepository.save(parent)
    }


    @Transactional(propagation = Propagation.REQUIRED)
    fun getUserByName(username: String): ParentUser? = userRepository.findUserByUsername(username).toUserDomain()

    fun isValid(user: UserDetails, password: String): Boolean {
        return encryptor.matches(password, user.password) && user.isAccountNonExpired
                && user.isAccountNonLocked && user.isCredentialsNonExpired
    }

}