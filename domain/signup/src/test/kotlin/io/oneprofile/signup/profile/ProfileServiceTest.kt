package io.oneprofile.signup.profile

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
        val profile = ProfileDomain("kong@gmail.com",
            "kong",
            "to",
            "000000000",
            Status.FREELANCE,
            ProfileStateDomain.Builder("kong@gmail.com").build(),
            Instant.now(),
            "portrait",
            "resume",
            "resume linkedin")
        Mockito.`when`(repository.create(profile)).thenReturn(profile)
        service = ProfileService(repository)

        // Act
        service.create(profile)

        // Arrange
        Mockito.verify(repository).create(profile)
    }

    @Test
    fun `should retrieve last profiles by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val profile1 = ProfileDomain("kong@gmail.com",
            "kong",
            "to",
            "000000000",
            Status.FREELANCE,
            ProfileStateDomain.Builder("kong@gmail.com").build(),
            Instant.now(),
            "portrait",
            "resume",
            "resume linkedin")

        val profile = Optional.of(profile1)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(profile)
        service = ProfileService(repository)

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertEquals(profile.get(), result)
    }

    @Test
    fun `should init the profile on signup status updated`() {
        // Arrange
        val username = "kong@gmail.com"
        val profile = ProfileDomain("kong@gmail.com",
            "kong",
            "to",
            "000000000",
            Status.FREELANCE,
            ProfileStateDomain.Builder("kong@gmail.com").validated(true).build(),
            Instant.now(),
            "portrait",
            "resume",
            "resume linkedin")

        Mockito.`when`(repository.findByUsername(username)).thenReturn(Optional.empty())
        Mockito.`when`(repository.create(profile)).thenReturn(profile)
        service = ProfileService(repository)

        // Act
        val result = service.create(profile)

        // Arrange
        Assertions.assertEquals(Status.FREELANCE, result.status)
    }

    @Test
    fun `should update the profile on signup state updated`() {
        // Arrange
        val username = "kong@gmail.com"
        val profile1 = ProfileDomain("kong@gmail.com",
            "kong",
            "to",
            "000000000",
            Status.FREELANCE,
            ProfileStateDomain.Builder("kong@gmail.com").registered(true).build(),
            Instant.now(),
            "portrait",
            "resume",
            "resume linkedin")

        val profile = Optional.of(profile1)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(profile)
        Mockito.`when`(repository.update(profile1)).thenReturn(profile1)
        service = ProfileService(repository)

        // Act
        val result = service.updateState(username, ProfileState.EMAIL_VALIDATED.toString())

        // Arrange
        Assertions.assertTrue(result.state?.emailValidated!!)
    }

    @Test
    fun `should update the profile on portrait uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new filename"
        val profile1 = ProfileDomain("kong@gmail.com",
            "kong",
            "to",
            "000000000",
            Status.FREELANCE,
            ProfileStateDomain.Builder("kong@gmail.com").validated(true).build(),
            Instant.now(),
            "portrait",
            "resume",
            "resume linkedin")
        val profile = Optional.of(profile1)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(profile)
        Mockito.`when`(repository.update(profile1)).thenReturn(profile1)
        service = ProfileService(repository)

        // Act
        val result = service.updateProfileOnPortraitUploaded(username, filename)

        // Arrange
        Assertions.assertTrue(result.state?.portraitUploaded!!)
        Assertions.assertEquals(filename, result.portraitFilename)
    }

    @Test
    fun `should update the profile on resume uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new filename"
        val profile1 = ProfileDomain("kong@gmail.com",
            "kong",
            "to",
            "000000000",
            Status.FREELANCE,
            ProfileStateDomain.Builder("kong@gmail.com").validated(true).build(),
            Instant.now(),
            "portrait",
            "resume",
            "resume linkedin")
        val profile = Optional.of(profile1)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(profile)
        Mockito.`when`(repository.update(profile1)).thenReturn(profile1)
        //Mockito.doNothing().`when`(repository).resumeUploaded(username, "new filename")
        service = ProfileService(repository)

        // Act
        val result = service.updateProfileOnResumeUploaded(username, filename)

        // Arrange
        Assertions.assertTrue(result.state?.resumeUploaded!!)
        Assertions.assertEquals(filename, result.resumeFilename)
    }

    @Test
    fun `should update the profile on resume linkedin uploaded`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new filename"
        val profile1 = ProfileDomain("kong@gmail.com",
            "kong",
            "to",
            "000000000",
            Status.FREELANCE,
            ProfileStateDomain.Builder("kong@gmail.com").validated(true).build(),
            Instant.now(),
            "portrait",
            "resume",
            "resume linkedin")
        val profile = Optional.of(profile1)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(profile)
        Mockito.`when`(repository.update(profile1)).thenReturn(profile1)
        //Mockito.doNothing().`when`(repository).resumeLinkedinUploaded(username, "new filename")
        service = ProfileService(repository)

        // Act
        val result = service.updateProfileOnResumeLinkedinUploaded(username, filename)

        // Arrange
        Assertions.assertTrue(result.state?.resumeLinkedinUploaded!!)
        Assertions.assertEquals(filename, result.resumeLinkedinFilename)
    }

    @Test
    fun `should update the profile resume`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new filename"
        val profile1 = ProfileDomain("kong@gmail.com",
            "kong",
            "to",
            "000000000",
            Status.FREELANCE,
            ProfileStateDomain.Builder("kong@gmail.com").validated(true).build(),
            Instant.now(),
            "portrait",
            "resume",
            "resume linkedin")
        val profile = Optional.of(profile1)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(profile)
        Mockito.`when`(repository.update(profile1)).thenReturn(profile1)
        Mockito.doNothing().`when`(repository).resumeUploaded(username, "new filename")
        service = ProfileService(repository)

        // Act
        val result = service.updateProfileResume(username, filename)

        // Arrange
        Assertions.assertTrue(result.state?.resumeUploaded!!)
        Assertions.assertEquals(filename, result.resumeFilename)
    }

    @Test
    fun `should update the profile resume linkedin`() {
        // Arrange
        val username = "kong@gmail.com"
        val filename = "new filename"
        val profile1 = ProfileDomain("kong@gmail.com",
            "kong",
            "to",
            "000000000",
            Status.FREELANCE,
            ProfileStateDomain.Builder("kong@gmail.com").validated(true).build(),
            Instant.now(),
            "portrait",
            "resume",
            "resume linkedin")
        val profile = Optional.of(profile1)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(profile)
        Mockito.`when`(repository.update(profile1)).thenReturn(profile1)
        Mockito.doNothing().`when`(repository).resumeLinkedinUploaded(username, "new filename")
        service = ProfileService(repository)

        // Act
        val result = service.updateProfileResumeLinkedin(username, filename)

        // Arrange
        Assertions.assertTrue(result.state?.resumeLinkedinUploaded!!)
        Assertions.assertEquals(filename, result.resumeLinkedinFilename)
    }
}