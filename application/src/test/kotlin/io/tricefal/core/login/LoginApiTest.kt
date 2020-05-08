package io.tricefal.core.login

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant


@ExtendWith(SpringExtension::class)
@ComponentScan("io.tricefal.core")
@Sql(scripts= ["/login.sql"], config = SqlConfig(encoding = "utf-8"))
@ActiveProfiles("test")
@DataJpaTest
//@EnableAutoConfiguration
class LoginApiTest {
    @Autowired
    lateinit var service: ILoginService<LoginModel, Long>

    @MockBean
    lateinit var repository: LoginJpaRepository

    @Test
    fun `should create a login successfully`() {
        // Arrange
        val login = LoginModel(1, "kong@gmail.com", Instant.now(), "ip", true)
        val loginEntity = toEntity(fromModel(login))
        Mockito.doNothing().`when`(repository).save(loginEntity)

        // Act
        service.create(login)

        // Arrange
        Mockito.verify(repository).save(loginEntity)

    }

    @Test
    fun `should retrieve last logins by username`() {
        // Arrange
        val username = "kong@gmail.com"
        val login1 = LoginModel(1, username, Instant.now(), "ip", true)
        val login2 = LoginModel(1, username, Instant.now(), "ip", true)
        val login3 = LoginModel(1, username, Instant.now(), "ip", true)

        val logins = arrayListOf(login1, login2, login3, login3)

        Mockito.`when`(repository.findByUsername(username)).thenReturn(logins.map { toEntity(fromModel(it)) })

        // Act
        val result = service.findByUsername(username)

        // Arrange
        Assertions.assertArrayEquals(logins.toArray(), result.toTypedArray())
    }
}