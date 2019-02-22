package com.home.smarthomeserver.graphqlmutations

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.home.smarthomeserver.controllers.DeviceController
import com.home.smarthomeserver.model.User
import com.home.smarthomeserver.models.Command
import com.home.smarthomeserver.security.Unsecured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*


@Component
class MutationResolver : GraphQLMutationResolver {

    @Autowired
    lateinit var controller: DeviceController

    @Unsecured
    fun signup(name: String, pass: String): User? =
            if (!(name.isBlank() && pass.isBlank())) {
                User(name, UUID.randomUUID().toString())
            } else {
                null
            }

    fun update(uId: String, deviceName: String, command: Command): String {
        controller.connect()
        controller.updateDeviceStatus(uId, deviceName, command)
        return "Ok"
    }
}