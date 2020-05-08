package io.tricefal.core.login

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant


@ExtendWith(MockitoExtension::class)
class LoginServiceTest {

    @Mock
    lateinit var repository: ILoginRepository<LoginDomain, Long>

    lateinit var service: ILoginService<LoginDomain, Long>

    @Test
    fun `should create a login successfully`() {
        // Arrange
        val login = LoginDomain(1, "kong@gmail.com", Instant.now(), "ip", true)
        Mockito.doNothing().`when`(repository).save(login)
        service = LoginService(repository)

        // Act
        service.create(login)

        // Arrange
        Mockito.verify(repository).save(login)
    }

    @Test
    fun `should retrieve last logins by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val login1 = LoginDomain(1, username, Instant.now(), "ip", true)
        val login2 = LoginDomain(1, username, Instant.now(), "ip", true)
        val login3 = LoginDomain(1, username, Instant.now(), "ip", true)

        val logins = arrayListOf(login1, login2, login3, login3)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(logins)
        service = LoginService(repository)

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertArrayEquals(logins.toArray(), result.toTypedArray())
    }
}