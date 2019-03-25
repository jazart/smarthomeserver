//package com.home.smarthomeserver.security
//
//import org.aspectj.lang.annotation.Aspect
//import org.aspectj.lang.annotation.Before
//import org.aspectj.lang.annotation.Pointcut
//import org.springframework.core.annotation.Order
//import org.springframework.security.authentication.AnonymousAuthenticationToken
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.stereotype.Component
//
//@Aspect
//@Component
//@Order(2)
//class GraphQLAspect {
//
//    @Throws(AccessDeniedException::class)
//    @Before("allGraphQLResolverMethods() && isDefinedInApplication() && !isMethodAnnotatedAsUnsecured()")
//    fun doSecurityCheck() {
//        val holder = SecurityContextHolder.getContext()
//        if (SecurityContextHolder.getContext() == null ||
//                SecurityContextHolder.getContext().authentication == null ||
//                !SecurityContextHolder.getContext().authentication.isAuthenticated ||
//                AnonymousAuthenticationToken::class.java.isAssignableFrom(SecurityContextHolder.getContext().authentication.javaClass)) {
//            throw org.springframework.security.access.AccessDeniedException("Unauthorized Access")
//        }
//    }
//
//    @Pointcut("target(com.coxautodev.graphql.tools.GraphQLResolver)")
//    private fun allGraphQLResolverMethods() {
//    }
//
//    @Pointcut("within(com.home.smarthomeserver..*)")
//    private fun isDefinedInApplication() {
//    }
//
//    /**
//     * Any method annotated with @Unsecured
//     */
//    @Pointcut("@annotation(com.home.smarthomeserver.security.Unsecured)")
//    private fun isMethodAnnotatedAsUnsecured() {
//    }
//
//}