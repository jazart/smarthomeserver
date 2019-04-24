package com.home.smarthomeserver.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.home.smarthomeserver.SignupException
import com.home.smarthomeserver.entity.ChildUserEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.models.*
import com.home.smarthomeserver.security.Unsecured
import com.home.smarthomeserver.service.DeviceService
import com.home.smarthomeserver.service.UserService
import graphql.GraphQLException
import graphql.schema.DataFetchingEnvironment
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

    private val mutationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Unsecured
    @Throws(SignupException::class)
    fun signup(creds: Credential, info: Personal): String? {
        val user = ParentUserEntity(firstName = info.firstName.trim(),
                lastName = info.lastName.trim(),
                password = creds.password.trim(),
                username = creds.username.trim(), id = 0L, email = info.email.trim())
        return userService.signUp(user)
    }

    @Unsecured
    @Throws(SignupException::class)
    fun login(name: String, pass: String): String? {
        return userService.login(name.trim(), pass.trim())
    }

    fun addChild(creds: Credential, personal: Personal, parentName: String): String? {
        val user = userService.userRepository.findUserByUsername(parentName) ?: return null
        val child = ChildUserEntity(firstName = personal.firstName,
                lastName = personal.lastName.trim(),
                password = creds.password.trim(),
                parent = user,
                id = 0,
                username = creds.username.trim(),
                email = personal.email.trim())
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

    suspend fun batchRemoveDevices(username: String, devices: List<String>, env: DataFetchingEnvironment): List<String>? {
//        val req = env.getContext<GraphQLContext>().httpServletRequest.get()
//        val headers = req.headerNames
//        val token = req.getHeader(TOKEN_PREFIX)

//        val auth = SecurityContextHolder.getContext().authentication
//        val name = auth.name
//        val authorities = auth.authorities
//
//        return mutationScope.async {
//            val result = mutableListOf<String>()
//            withContext(Dispatchers.IO) {
//                devices.forEach {
//                    if (deviceService.removeDevice(DeviceInfo(username, it))) result.add(it)
//                }
//                return@withContext result.toList()
//            }
//        }.await()
        return null
    }

    fun addFavorite(deviceInfo: DeviceInfo): String? {
        try {
            deviceService.addFavorite(deviceInfo)
            return deviceInfo.deviceName
        } catch (e: Exception) {
            throw Exception("Unable to add ${deviceInfo.deviceName} as favorite")
        }
    }

    fun removeFavorite(deviceInfo: DeviceInfo): String? {
        try {
            deviceService.removeFavorite(deviceInfo)
            return deviceInfo.deviceName
        } catch (e: Exception) {
            throw Exception("Unable to remove ${deviceInfo.deviceName} from favorite")
        }
    }
}
