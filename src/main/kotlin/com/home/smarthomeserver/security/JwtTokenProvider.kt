package com.home.smarthomeserver.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest


@Component
class JwtTokenProvider {

    @Autowired
    lateinit var userDetailsServiceImpl: UserDetailsServiceImpl

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    fun createToken(username: String): String =
            JWT.create().run {
                withSubject(userDetailsServiceImpl.loadUserByUsername(username).username)
                withIssuedAt(Date())
                withExpiresAt(Date(System.currentTimeMillis() + EXP_TIME))
                withIssuer("Zenith")
                sign(Algorithm.HMAC512(SECRET.toByteArray()))
            }

    @Throws(UsernameNotFoundException::class)
    fun getAuthentication(token: String): Authentication {
        val userName = getUsername(token)
        val details = userDetailsServiceImpl.loadUserByUsername(userName)
        return UsernamePasswordAuthenticationToken(details, details.password, details.authorities)
    }

    fun getUsername(token: String): String =
            JWT.decode(token).subject


    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader(HEADER_STRING)
        return if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            bearerToken.substring(7)
        } else null
    }

    @Throws(JWTVerificationException::class)
    fun validateToken(token: String): Boolean {
        try {
            val verifier = JWT.require(Algorithm.HMAC512(SECRET))
                    .withIssuer("Zenith")
                    .withSubject(getUsername(token))
                    .acceptExpiresAt(EXP_LEEWAY)
                    .build()
            verifier.verify(token)
            return true
        } catch (e: JWTVerificationException) {
            throw JWTVerificationException("Expired or invalid JWT token")
        }

    }

    companion object {
        const val SECRET = "SecretKeyToGenJWTs"
        const val EXP_TIME = 864_000_000
        const val TOKEN_PREFIX = "Bearer "
        const val HEADER_STRING = "Authorization"
        const val EXP_LEEWAY = 180_000L
    }
}