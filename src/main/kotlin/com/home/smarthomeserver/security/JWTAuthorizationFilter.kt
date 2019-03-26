package com.home.smarthomeserver.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JWTAuthorizationFilter(private val jwtTokenProvider: JwtTokenProvider)
    : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val token = jwtTokenProvider.resolveToken(request)
        if (token != null && jwtTokenProvider.validateToken(token)) {
            try {
                SecurityContextHolder.getContext().authentication = jwtTokenProvider.getAuthentication(token)
            } catch (e: Exception) {
                // Log error
            } finally {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found")
                chain.doFilter(request, response)
            }
        }
        chain.doFilter(request, response)
    }
}