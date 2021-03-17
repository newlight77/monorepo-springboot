package io.tricefal.core.user

import io.tricefal.core.InfrastructureMockBeans
import io.tricefal.core.keycloak.KeycloakRegistrationService
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
class UserWebHandlerTest {

    @Autowired
    lateinit var keycloakRegistrationService: KeycloakRegistrationService

    @Autowired
    lateinit var userWebHandler: UserWebHandler

    @Test
    fun `should update the user password successfully`() {
        // Arrange
        val user = UserPasswordModel("kong@gmail.com", "newpassword")
        Mockito.`when`(keycloakRegistrationService.updatePassword("kong@gmail.com", "newpassword")).thenReturn(true)
//        Mockito.doNothing().`when`(userDataAdapter).updatePassword(user.username, user.newPassword)

        // Act
        val result = userWebHandler.updatePassword(user.username, user.newPassword)

        // Arrange
        Assertions.assertTrue(result)
//        Mockito.verify(userDataAdapter).updatePassword("kong@gmail.com", "newpassword")

    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}