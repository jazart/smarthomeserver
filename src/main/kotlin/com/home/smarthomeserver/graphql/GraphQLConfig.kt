//package com.home.smarthomeserver.graphql
//
//import com.coxautodev.graphql.tools.SchemaParserOptions
//import kotlinx.coroutines.ThreadContextElement
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.core.context.SecurityContext
//import org.springframework.security.core.context.SecurityContextHolder
//import kotlin.coroutines.AbstractCoroutineContextElement
//import kotlin.coroutines.CoroutineContext
//
//@Configuration
//class GraphQLConfig {
//
//    @Bean
//    fun schemaParserOptions(): SchemaParserOptions = SchemaParserOptions.newOptions()
//            .coroutineCon(SecurityContextCoroutineProvider())
//            .build()
//
//}
//
//internal class SecurityContextCoroutineProvider : CoroutineContextProvider {
//
//    override fun provide(): CoroutineContext = SecurityContextElement()
//}
//
//internal class SecurityContextElement : ThreadContextElement<SecurityContext>, AbstractCoroutineContextElement(Key) {
//    companion object Key : CoroutineContext.Key<SecurityContextElement>
//
//    val currentContext = SecurityContextHolder.getContext()
//    val initial = currentContext.authentication
//
//    override fun updateThreadContext(context: CoroutineContext): SecurityContext {
//        return currentContext
//    }
//
//    override fun restoreThreadContext(context: CoroutineContext, oldState: SecurityContext) {
//    }
//}
//
//interface CoroutineContextProvider {
//    fun provide(): CoroutineContext
//}