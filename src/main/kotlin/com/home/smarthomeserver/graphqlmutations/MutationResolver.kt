package com.home.smarthomeserver.graphqlmutations

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.home.smarthomeserver.User
import org.springframework.stereotype.Component
import java.util.*


@Component
class MutationResolver : GraphQLMutationResolver {

    fun signup(name: String, pass: String): User? =
            if (!(name.isBlank() && pass.isBlank())) {
                User(name, UUID.randomUUID().toString())
            } else {
                null
            }
}