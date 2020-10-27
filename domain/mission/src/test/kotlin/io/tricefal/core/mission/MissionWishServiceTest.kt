package io.tricefal.core.mission

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class MissionWishServiceTest {

    @Mock
    lateinit var adapter: IMissionWishAdapter

    lateinit var service: IMissionWishService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(adapter)
    }

    @Test
    fun `should do a create`() {
        // Arranges
        val missionWish = MissionWishDomain.Builder("kong@gmail.com")
                .build()

        Mockito.`when`(adapter.create(missionWish)).thenReturn(missionWish)

        service = MissionWishService(adapter)

        // Act
        val result = service.create(missionWish)

        // Arrange
        Mockito.verify(adapter).create(missionWish)
        Assertions.assertEquals(missionWish.username, result.username)
    }

    @Test
    fun `should find a missionWish by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val missionWish = MissionWishDomain.Builder(username)
                .build()

        Mockito.`when`(adapter.findByUsername(username)).thenReturn(Optional.of(missionWish))

        service = MissionWishService(adapter)

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertEquals(missionWish.username, result.username)
    }

}