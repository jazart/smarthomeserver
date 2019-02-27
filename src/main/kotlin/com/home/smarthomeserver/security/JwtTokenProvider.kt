package com.home.smarthomeserver.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest


@Component
class JwtTokenProvider {

    @Autowired
    lateinit var userDetailsServiceImpl: UserDetailsServiceImpl


    fun createToken(name: String): String =
            JWT.create().run {
                withSubject(userDetailsServiceImpl.loadUserByUsername(name).username)
                withExpiresAt(Date(System.currentTimeMillis() + EXP_TIME))
                withIssuer("Zenith")
                sign(Algorithm.HMAC512(SECRET.toByteArray()))
            }


    fun getAuthentication(token: String): Authentication {
        val userName = getUsername(token)
        val details = userDetailsServiceImpl.loadUserByUsername(userName)
        return UsernamePasswordAuthenticationToken(details, "", details.authorities)
    }

    fun getUsername(token: String): String =
            JWT.decode(token).subject


    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader(HEADER_STRING)
        return if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            bearerToken.substring(7)
        } else null
    }

    fun validateToken(token: String): Boolean {
        try {
            JWT.require(Algorithm.HMAC512(SECRET)).withIssuer("Zenith").withSubject(getUsername(token))
            return true
        } catch (e: IllegalArgumentException) {
            throw Exception("Expired or invalid JWT token")
        }

    }

    companion object {
        const val SECRET = "SecretKeyToGenJWTs"
        const val EXP_TIME = 864_000_000
        const val TOKEN_PREFIX = "Bearer "
        const val HEADER_STRING = "Authorization"
    }
}