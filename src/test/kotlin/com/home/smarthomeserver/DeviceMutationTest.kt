package com.home.smarthomeserver

import com.home.smarthomeserver.entity.DeviceEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.models.DeviceInfo
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK ,classes = arrayOf(SmarthomeserverApplication::class))
class DeviceMutationTest {
    @LocalServerPort
    private val port = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var deviceService :DeviceService

    @MockBean
    lateinit var deviceRepository: DeviceRepository

    //@Autowired
    //lateinit var userRepo: UserRepository<ParentUserEntity>
    @Before
    fun setup(){
        var newUserEntity = ParentUserEntity(username = "Ken", password = "random", email = "email", id = 45L)
        var newDevice = DeviceEntity(id = 64L,
                thingName = "Camera",
                owner = newUserEntity)
        Mockito.`when`(deviceRepository.findDeviceEntityByNameAndOwnerUsername("","Ken")).thenReturn(newDevice)
    }
    @Test
    fun `test add device should add to database`(){

        runBlocking {
            //deviceService.addDevice(deviceInfo = DeviceInfo(username = "Ken", deviceName = ""))
            //val dbDevice = deviceService.deviceRepository.findDeviceEntityByNameAndOwnerUsername(newDevice.name, "Ken")
            //assertThat(newDevice).isNotEqualTo(dbDevice)
        }
    }


}
@TestConfiguration
class TestConfig{
    @Bean
    fun deviceService() = DeviceService()
}