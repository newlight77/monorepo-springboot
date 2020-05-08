package io.tricefal.core.signup

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.util.*

@ExtendWith(MockitoExtension::class)
class SignupServiceTest {

    @Mock
    lateinit var repository: ISignupRepository

    lateinit var service: ISignupService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(repository)
    }

    @Test
    fun `should do a signup`() {
        // Arrange
        val signup = SignupDomain( 0, "kong@gmail.com", "kong", "to",
                "1234567890", Instant.now(), "123456", Status.FREELANCE)

        Mockito.doNothing().`when`(repository).save(signup)

        service = SignupService(repository)

        // Act
        service.signup(signup)

        // Arrange
        Mockito.verify(repository).save(signup)
    }

    @Test
    fun `should find a signup by username`() {
        // Arrange
        val username ="kong@gmail.com"
        val signup = SignupDomain( 0, username, "kong", "to",
                "1234567890", Instant.now(), "123456", Status.FREELANCE)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(Optional.of(signup))

        service = SignupService(repository)

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertEquals(signup, result.get())
    }

    @Test
    fun `should update the signup status`() {
        // Arrange
        val username ="kong@gmail.com"
        val signup = SignupDomain( 0, username, "kong", "to",
                "1234567890", Instant.now(), "123456", Status.FREELANCE)
        val expected = SignupDomain( 0, username, "kong", "to",
                "1234567890", signup.signupDate, "123456", Status.EMPLOYEE)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(Optional.of(signup))
        Mockito.doNothing().`when`(repository).update(expected)

        service = SignupService(repository)

        // Act
        service.updateStatus(username, Status.EMPLOYEE)

        // Arrange

        Mockito.verify(repository).findByUsername(username)
        Mockito.verify(repository).update(expected)
    }
}