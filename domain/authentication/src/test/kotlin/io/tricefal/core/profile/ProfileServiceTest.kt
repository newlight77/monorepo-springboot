package io.tricefal.core.profile

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.util.*


@ExtendWith(MockitoExtension::class)
class ProfileServiceTest {

    @Mock
    lateinit var repository: ProfileDataAdapter

    lateinit var service: IProfileService

    @Test
    fun `should create a profile successfully`() {
        // Arrange
        val profile = ProfileDomain("kong@gmail.com", Instant.now(), "portrait", "resume", "resume linkedin")
        Mockito.`when`(repository.save(profile)).thenReturn(profile)
        service = ProfileService(repository)

        // Act
        service.save(profile)

        // Arrange
        Mockito.verify(repository).save(profile)
    }

    @Test
    fun `should retrieve last profiles by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val profile1 = ProfileDomain(username, Instant.now(), "portrait", "resume", "resume linkedin")

        val profile = Optional.of(profile1)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(profile)
        service = ProfileService(repository)

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertEquals(profile, result)
    }

    @Test
    fun `should update the profile on portrait uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new filename"
        val profile1 = ProfileDomain(username, Instant.now(), "portrait", "resume", "resume linkedin")

        val profile = Optional.of(profile1)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(profile)
        service = ProfileService(repository)

        // Act
        val result = service.updateProfileOnPortraitUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(filename, result.portraitFilename)
    }

    @Test
    fun `should update the profile on resume uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new filename"
        val profile1 = ProfileDomain(username, Instant.now(), "portrait", "resume", "resume linkedin")

        val profile = Optional.of(profile1)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(profile)
        service = ProfileService(repository)

        // Act
        val result = service.updateProfileOnResumeUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(filename, result.resumeFilename)
    }

    @Test
    fun `should update the profile on resume linkedin uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new filename"
        val profile1 = ProfileDomain(username, Instant.now(), "portrait", "resume", "resume linkedin")

        val profile = Optional.of(profile1)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(profile)
        service = ProfileService(repository)

        // Act
        val result = service.updateProfileOnResumeLinkedinUploaded(username, filename)

        // Arrange
        Assertions.assertEquals(filename, result.resumeLinkedinFilename)
    }
}