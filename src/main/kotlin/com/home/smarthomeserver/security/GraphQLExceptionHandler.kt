package com.home.smarthomeserver.security

import graphql.ErrorType
import graphql.GraphQLError
import graphql.language.SourceLocation
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler

@Component
class GraphQLExceptionHandler {

    @ExceptionHandler(Throwable::class)
    fun handleException(e: Throwable): GraphQLError {
        return ThrowableGraphQLError(e)
    }
}

class ThrowableGraphQLError(private val e: Throwable) : GraphQLError {
    override fun getMessage(): String = e.message ?: ""

    override fun getErrorType(): ErrorType = ErrorType.DataFetchingException

    override fun getLocations(): MutableList<SourceLocation> = mutableListOf()
}