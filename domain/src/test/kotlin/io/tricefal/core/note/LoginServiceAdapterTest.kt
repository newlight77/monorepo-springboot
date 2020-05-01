package io.tricefal.core.note

import io.tricefal.core.login.ILoginRepository
import io.tricefal.core.login.ILoginService
import io.tricefal.core.login.LoginDomain
import io.tricefal.core.login.LoginService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant


@ExtendWith(MockitoExtension::class)
class LoginServiceAdapterTest {

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
}