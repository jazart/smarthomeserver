package com.home.smarthomeserver.graphqlmutations

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.home.smarthomeserver.ChildUserRepository
import com.home.smarthomeserver.DeviceService
import com.home.smarthomeserver.SignupException
import com.home.smarthomeserver.UserService
import com.home.smarthomeserver.entity.ChildUserEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.models.*
import com.home.smarthomeserver.security.Unsecured
import graphql.GraphQLException
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.iot.model.ResourceAlreadyExistsException
import software.amazon.awssdk.services.iot.model.ThrottlingException


@Component
class MutationResolver : GraphQLMutationResolver {

    @Autowired
    lateinit var deviceService: DeviceService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var childUserRepository: ChildUserRepository

    private val mutationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Unsecured
    @Throws(SignupException::class)
    fun signup(creds: Credential, info: Personal): String? {
        val user = ParentUserEntity(firstName = info.firstName,
                lastName = info.lastName,
                password = creds.password,
                username = creds.username, id = 0L, email = info.email)
        return userService.signUp(user)
    }

    @Unsecured
    @Throws(SignupException::class)
    fun login(name: String, pass: String): String? {
        return userService.login(name, pass)
    }

    fun addChild(creds: Credential, personal: Personal, parentName: String): String? {
        val user = userService.userRepository.findUserByUsername(parentName)
        val child = ChildUserEntity(firstName = personal.firstName,
                lastName = personal.lastName,
                password = creds.password,
                parent = user,
                id = 0,
                username = creds.username,
                email = personal.email)
        userService.addChild(user, child)
        return "Ok"
    }

    @Unsecured
    fun sendCommand(deviceInfo: DeviceInfo, deviceType: DeviceType, command: Command): Command? {
        deviceService.run {
            updateDeviceStatus(deviceInfo, command)
        }
        return command
    }


    @Unsecured
    @Throws(GraphQLException::class)
    suspend fun removeDevice(deviceInfo: DeviceInfo): String? {
        return mutationScope.async {
            withContext(Dispatchers.IO) {
                try {
                    return@withContext if (deviceService.removeDevice(deviceInfo))
                        deviceInfo.deviceName
                    else
                        null
                } catch (e: NoSuchElementException) {
                    return@withContext null
                }
            }
        }.await()
    }

    fun modifyDeviceName(deviceInfo: DeviceInfo, newName: String): String? {
        deviceService.modifyDeviceName(deviceInfo, newName)
        return newName
    }

    @Unsecured
    @Throws(Exception::class)
    suspend fun addDevice(deviceInfo: DeviceInfo, type: DeviceType): String? {
        return mutationScope.async {
            try {
                withContext(Dispatchers.IO) {
                    return@withContext if (deviceService.addDevice(deviceInfo))
                        deviceInfo.deviceName
                    else
                        null
                }
            } catch (e: ThrottlingException) {
                return@async null
            } catch (e: ResourceAlreadyExistsException) {
                throw Exception("The device ${deviceInfo.deviceName} already exists in your device group.")
            }
        }.await()
    }

    fun batchRemoveDevices(username: String, devices: List<String>): List<String>? {
        return null
    }
    fun addFavorite(deviceInfo: DeviceInfo): String? {
        return null
    }

    fun removeFavorite(deviceInfo: DeviceInfo): String? {
       return null
    }

}