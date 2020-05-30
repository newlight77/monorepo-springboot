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
        val signup = SignupDomain.Builder("kong@gmail.com")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .status(Status.FREELANCE)
                .build()

        Mockito.`when`(repository.save(signup)).thenReturn(signup)

        service = SignupService(repository)

        // Act
        val result = service.signup(signup)

        // Arrange
        Mockito.verify(repository).save(signup)
        Assertions.assertNotEquals("", result.activationCode)
    }

    @Test
    fun `should find a signup by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val signup = SignupDomain.Builder(username)
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .status(Status.FREELANCE)
                .build()

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
        val username = "kong@gmail.com"
        val signup = SignupDomain.Builder(username)
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .status(Status.FREELANCE)
                .build()

        val expected = SignupDomain.Builder(username)
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(signup.signupDate)
                .activationCode("123456")
                .status(Status.EMPLOYEE)
                .build()

        Mockito.`when`(repository.findByUsername(username)).thenReturn(Optional.of(signup))
        Mockito.`when`(repository.update(expected)).thenReturn(signup)

        service = SignupService(repository)

        // Act
        service.updateStatus(username, Status.EMPLOYEE)

        // Arrange
        Mockito.verify(repository).findByUsername(username)
        Mockito.verify(repository).update(expected)
    }
}