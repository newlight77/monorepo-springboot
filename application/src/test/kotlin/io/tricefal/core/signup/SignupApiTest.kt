package io.tricefal.core.signup

import io.tricefal.core.login.SignupJpaRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class, MockitoExtension::class)
@ComponentScan("io.tricefal.core")
@ActiveProfiles("test")
@DataJpaTest
class SignupApiTest {
    @Autowired
    lateinit var service: SignupHandler

    @MockBean
    lateinit var repository: SignupJpaRepository

    @Test
    fun `should do a signup`() {
        // Arrange
        val signup = SignupModel( "kong@gmail.com", "password", "kong", "to", "1234567890", Instant.now())
        signup.activationCode = "123455"
        signup.status = Status.FREELANCE

        val signupEntity = toEntity(fromModel(signup))
        Mockito.doNothing().`when`(repository).save(signupEntity)

        // Act
        service.signup(signup)

        // Arrange
        Mockito.verify(repository).save(signupEntity)
    }

    @Test
    fun `should find a signup by username`() {
        // Arrange
        val username ="kong@gmail.com"
        val signup = SignupModel( username, "password", "kong", "to",
                "1234567890", Instant.now(), "123456", Status.FREELANCE)

        val signupEntity = toEntity(fromModel(signup))
        Mockito.`when`(repository.findByUsername(username)).thenReturn(Optional.of(signupEntity))

        // Act
        val result = service.findByUsername(username)

        // Arrange
        val expected = SignupModel( username, "", "kong", "to",
                "1234567890", signup.signupDate, "123456", Status.FREELANCE)
        Assertions.assertTrue(result.isPresent)
        Assertions.assertEquals(expected, result.get())
    }

    @Test
    fun `should update the signup status`() {
        // Arrange
        val username ="kong@gmail.com"
        val signup = SignupModel( username, "password", "kong", "to",
                "1234567890", Instant.now(), "123456", Status.FREELANCE)
        val expected = SignupEntity( 0, username, "kong", "to",
                "1234567890", signup.signupDate, "123456", Status.EMPLOYEE.toString())

        val signupEntity = toEntity(fromModel(signup))
        Mockito.`when`(repository.findByUsername(username)).thenReturn(Optional.of(signupEntity))
        Mockito.doNothing().`when`(repository).save(expected)

        // Act
        val result = service.updateStatus(username, Status.EMPLOYEE.toString())

        // Arrange
        Mockito.verify(repository).save(expected)
    }
}