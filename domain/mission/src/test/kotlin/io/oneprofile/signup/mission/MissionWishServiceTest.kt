package io.oneprofile.signup.mission

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
    lateinit var dataAdapter: MissionWishDataAdapter

    lateinit var service: IMissionWishService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(dataAdapter)
    }

    @Test
    fun `should do a create`() {
        // Arranges
        val missionWish = MissionWishDomain.Builder("kong@gmail.com")
                .build()

        Mockito.`when`(dataAdapter.create(missionWish)).thenReturn(missionWish)

        service = MissionWishService(dataAdapter)

        // Act
        val result = service.create(missionWish)

        // Arrange
        Mockito.verify(dataAdapter).create(missionWish)
        Assertions.assertEquals(missionWish.username, result.username)
    }

    @Test
    fun `should find a missionWish by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val missionWish = MissionWishDomain.Builder(username)
                .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(missionWish))

        service = MissionWishService(dataAdapter)

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertEquals(missionWish.username, result.username)
    }

    @Test
    fun `should update upon mission specific resume uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new-resume.pdf"
        val missionWish = MissionWishDomain.Builder(username)
            .build()

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.of(missionWish))

        service = MissionWishService(dataAdapter)

        // Act
        val result = service.updateOnResumeUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(result.resumeFilename, filename)
    }

    @Test
    fun `should create a new mission upon mission specific resume uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new-resume.pdf"

        Mockito.`when`(dataAdapter.findByUsername(username)).thenReturn(Optional.empty())

        service = MissionWishService(dataAdapter)

        // Act
        val result = service.updateOnResumeUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(result.username, username)
        Assertions.assertEquals(result.resumeFilename, filename)
    }

}