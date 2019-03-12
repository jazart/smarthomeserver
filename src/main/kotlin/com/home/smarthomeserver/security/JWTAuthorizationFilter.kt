package com.home.smarthomeserver.security

import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JWTAuthorizationFilter(private val jwtTokenProvider: JwtTokenProvider)
    : OncePerRequestFilter() {

    @Throws(UsernameNotFoundException::class, JWTVerificationException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val token = jwtTokenProvider.resolveToken(request)
        if (token != null && jwtTokenProvider.validateToken(token)) {
            val auth = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = auth
        }
        chain.doFilter(request,response)
    }
}