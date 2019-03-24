package com.home.smarthomeserver

import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.repository.ParentUserRepository
import com.home.smarthomeserver.repository.UserRepository
import com.home.smarthomeserver.security.JwtTokenProvider
import com.home.smarthomeserver.security.UserDetailsServiceImpl
import com.home.smarthomeserver.service.UserService
import junit.framework.TestCase.fail
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [SmarthomeserverApplication::class])
class UserServiceTest{

    @Autowired
    lateinit var userService: UserService

    @MockBean
    lateinit var userRepo: ParentUserRepository

    @MockBean
    lateinit var detailService: UserDetailsServiceImpl

    @MockBean
    lateinit var tokenProvider: JwtTokenProvider

    @MockBean
    lateinit var encoder: BCryptPasswordEncoder

    lateinit var user: ParentUserEntity

    @Before
    fun setup(){
        user = ParentUserEntity(username = "Ken", password = "random11", email = "email", id = 45L)
        val principle = with(User.builder()){
            accountExpired(false)
            accountLocked(false)
            username("Ken")
            password("random11")
            authorities(listOf())
        }.build()
        Mockito.`when`(userRepo.existsParentUserEntityByUsername("Ken")).thenReturn(true)
        Mockito.`when`(userRepo.findUserByUsername("Ken")).thenReturn(user)
        Mockito.`when`(detailService.loadUserByUsername("Ken")).thenReturn(principle)
        Mockito.`when`(userRepo.existsParentUserEntityByEmail("email")).thenReturn(true)
        Mockito.`when`(tokenProvider.createToken("Ken")).thenReturn("token")
        Mockito.`when`(userRepo.save(ArgumentMatchers.any(ParentUserEntity::class.java))).thenReturn(user)
        Mockito.`when`(encoder.matches("random11", "random11")).thenReturn(true)
        Mockito.`when`(encoder.encode("random11")).thenReturn("encodedPassword")
    }

    @Test
    fun `test sign up with existing username should through exception`(){
        try{
            userService.signUp(user)
        }catch (e : SignupException){
            return
        }
        fail()
    }

    @Test
    fun `test sign up with existing email should through exception`(){
        try{
            userService.signUp(user)
        }catch (e : SignupException){
            return
        }
        fail()
    }

    @Test
    fun `test sign up with proper credentials returns token`(){
        Mockito.`when`(tokenProvider.createToken("Kent")).thenReturn("token")
        val token = userService.signUp(ParentUserEntity(username = "Kent", password = "random11", email = "email2", id = 45L))
        assert(token.isNotBlank())
    }

    @Test
    fun `test login with incorrect credentials throws exception`(){
        try {
            val newUser = ParentUserEntity(username = "Kent", password = "random11", email = "email2", id = 45L)
            //val loginReponse = userService.login(newUser.username,newUser.password)
            userService.login(newUser.username, newUser.password)
        }catch (e : SignupException){
            return
        }
        fail()
    }

    @Test
    fun `test login with correct credentials returns token`(){
        val token = userService.login(user.username,user.password)
        assert(token.isNotBlank())
    }
}

