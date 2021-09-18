package io.oneprofile.core.login

import io.oneprofile.core.InfrastructureMockBeans
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.time.Instant


//@ExtendWith(SpringExtension::class, MockitoExtension::class)
@ExtendWith(MockitoExtension::class)
//@Sql(scripts= ["/login.sql"], config = SqlConfig(encoding = "utf-8"))
@ActiveProfiles("test")
@DataJpaTest
@ContextConfiguration(classes = [InfrastructureMockBeans::class])
class LoginWebHandlerTest {
    @Autowired
    lateinit var loginWebHandler: LoginWebHandler

    @MockBean
    lateinit var loginJpaRepository: LoginJpaRepository

    @Test
    fun `should create a login successfully`() {
        // Arrange
        val login = LoginModel("kong@gmail.com", Instant.now(), "ip", "paris", "ile de france", "firefox on Iphone11", true)
        val loginEntity = toEntity(fromModel(login))
        Mockito.doNothing().`when`(loginJpaRepository).save(loginEntity)

        // Act
        loginWebHandler.login(login)

        // Arrange
        Mockito.verify(loginJpaRepository).save(loginEntity)

    }

    @Test
    fun `should retrieve last logins by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val login1 = LoginModel(username, Instant.now(), "ip", "paris", "ile de france", "firefox", true)
        val login2 = LoginModel(username, Instant.now(), "ip", "paris", "ile de france", "firefox", true)
        val login3 = LoginModel(username, Instant.now(), "ip", "paris", "ile de france", "firefox", true)

        val logins = arrayListOf(login1, login2, login3)

        Mockito.`when`(loginJpaRepository.findByUsername(username)).thenReturn(logins.map { toEntity(fromModel(it)) })

        // Act
        val result = loginWebHandler.findByUsername(username)

        // Arrange
        Assertions.assertArrayEquals(logins.toArray(), result.toTypedArray())
    }
}