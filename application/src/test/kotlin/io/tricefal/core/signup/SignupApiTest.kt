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
        val signup = SignupModel.Builder("kong@gmail.com")
                .password("password")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .status(Status.FREELANCE)
                .build()

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
        val username = "kong@gmail.com"
        val signup = SignupModel.Builder(username)
                .password("password")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .status(Status.FREELANCE)
                .build()

        val expected = SignupModel.Builder(username)
                .password("")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(signup.signupDate)
                .activationCode("123456")
                .status(Status.FREELANCE)
                .build()

        val signupEntity = toEntity(fromModel(signup))
        Mockito.`when`(repository.findByUsername(username)).thenReturn(Optional.of(signupEntity))

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertTrue(result.isPresent)
        Assertions.assertEquals(expected.username, result.get().username)
        Assertions.assertEquals(null, result.get().password)
    }

    @Test
    fun `should update the signup status`() {
        // Arrange
        val username ="kong@gmail.com"
        val signup = SignupModel.Builder(username)
                .password("password")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .status(Status.FREELANCE)
                .build()

        val expected = SignupEntity( null, username, "kong", "to",
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