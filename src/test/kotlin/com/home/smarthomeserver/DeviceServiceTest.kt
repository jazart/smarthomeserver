package com.home.smarthomeserver

import com.home.smarthomeserver.entity.DeviceEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.models.DeviceInfo
import com.home.smarthomeserver.repository.DeviceRepository
import com.home.smarthomeserver.repository.ParentUserRepository
import com.home.smarthomeserver.service.DeviceService
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [SmarthomeserverApplication::class])
class DeviceServiceTest {

    @Autowired
    lateinit var deviceService: DeviceService

    @MockBean
    lateinit var deviceRepository: DeviceRepository

    @MockBean
    lateinit var userRepository: ParentUserRepository


    lateinit var newDevice: DeviceEntity

    @Before
    fun setup() {
        val newUserEntity = ParentUserEntity(username = "Ken", password = "random", email = "email", id = 45L)
        newDevice = DeviceEntity(
                name = "test",
                id = 64L,
                thingName = "test" + newUserEntity.id,
                owner = newUserEntity,
                favorite = true)
        Mockito.`when`(deviceRepository.findDeviceEntityByNameAndOwnerUsername("test", "Ken"))
                .thenReturn(newDevice)
        Mockito.`when`(userRepository.findUserByUsername("Ken")).thenReturn(newUserEntity)
        Mockito.`when`(deviceRepository.save(any(DeviceEntity::class.java))).thenReturn(newDevice)
    }

    @Test
    fun `test add device should add to database`() {
        runBlocking {
            // Arrange
            val newDevice = deviceRepository.findDeviceEntityByNameAndOwnerUsername("test", "Ken")
                    ?: fail("Device not found")

            // Act
            val didDeviceAdd = deviceService.addDevice(deviceInfo = DeviceInfo(username = "Ken", deviceName = "test"))

            // Assert
            assert(didDeviceAdd)
        }
    }

    @Test
    fun `test remove device should remove from AWS`() {
        runBlocking {
            //Act
            val didDeviceRemove = deviceService.removeDevice(deviceInfo = DeviceInfo(username = "Ken", deviceName = "test"))

            //Assert
            assert(didDeviceRemove)
        }
    }

    @Test
    fun `test remove invalid device should return false`() {
        runBlocking {
            //Act
            val didDeviceRemove = deviceService.removeDevice(deviceInfo = DeviceInfo(username = "Ken", deviceName = "newTest"))

            //Assert
            assertThat(didDeviceRemove).isFalse()
        }
    }

    @Test
    fun `test add favorite should return name of new favorite`() {
        val newDeviceInfo = DeviceInfo(username = "Ken", deviceName = "test")
        val favoriteName = deviceService.addFavorite(newDeviceInfo)
        assertThat(favoriteName).isEqualTo("test")
    }

    @Test
    fun `test remove favorite should name of removed favorite`() {
        val newDeviceInfo = DeviceInfo(username = "Ken", deviceName = "test")
        val favoriteName = deviceService.removeFavorite(newDeviceInfo)
        assertThat(favoriteName).isEqualTo("test")
        assertThat(newDevice.favorite).isFalse()
    }

    @Test
    fun `test modify device should return new name`(){
        val newDeviceInfo = DeviceInfo(username = "Ken", deviceName = "test")
        val newName = deviceService.modifyDeviceName(newDeviceInfo, "newTest")
        assertThat(newName).isEqualTo("newTest")

    }
}

@TestConfiguration
class TestConfig {
    @Bean
    fun deviceService() = DeviceService()
}