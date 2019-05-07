package com.home.smarthomeserver.repository

import com.home.smarthomeserver.entity.DeviceEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.models.DeviceType
import org.assertj.core.api.Assertions.fail
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.annotation.Commit
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeviceRepositoryTests {

    @Autowired
    lateinit var deviceRepository: DeviceRepository

    @Autowired
    lateinit var userRepository: ParentUserRepository

    private final val parent = ParentUserEntity("jaz", id = 1L, email = "", password = "1")
    val device = DeviceEntity("test", 1L, owner = parent, thingName = "thing", type = DeviceType.CAMERA)

    @Before
    @Commit
    fun setup() {
        println("-----SETUP-----")
        deviceRepository.save(device)
        println("-----SETUP-----")
    }

    @Test
    fun `should throw error if duplicate device name added`() {
        // Arrange
        val deviceDup = DeviceEntity("test", 2L, owner = parent, thingName = "t", type = DeviceType.CAMERA)

        // Act
        try {
            deviceRepository.save(deviceDup)
        } catch (e: DataIntegrityViolationException) {
            return
        }

        // Assert
        fail<String>("Duplicate device was added")
    }

    @Test
    fun `should throw error if duplicate Thing name added`() {
        // Arrange
        val deviceDup = DeviceEntity("aTest", 2L, owner = parent, thingName = "thing", type = DeviceType.CAMERA)
        // Act
        try {
            deviceRepository.save(deviceDup)
        } catch (e: DataIntegrityViolationException) {
            return
        }
        // Assert
        fail<String>("Duplicate device was added")
    }

    @After
    @Commit
    fun teardown() {
        println("-----TEARDOWN-----")
        parent.devices.remove(device)
        userRepository.save(parent)
        println("-----TEARDOWN-----")
    }
}