package io.oneprofile.core.keycloak

import io.oneprofile.core.signup.SignupDomain
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.core.env.Environment

internal class KeycloakRegistrationServiceTest {

    @Mock
    val env: Environment = Mockito.mock(Environment::class.java)

    // purposely ignore because this test is used to validate th integration with keycloak
//     @Test
    fun `should create a user on Keycloak`() {
        // arrange
        Mockito.`when`(env.getProperty("keycloak.base-url")).thenReturn("http://localhost:1080")
        Mockito.`when`(env.getProperty("keycloak.app.realm")).thenReturn("local.app")
        Mockito.`when`(env.getProperty("keycloak.admin.realm")).thenReturn("master")
        Mockito.`when`(env.getProperty("keycloak.admin.client-id")).thenReturn("admin-cli")
        Mockito.`when`(env.getProperty("keycloak.admin.client-secret")).thenReturn("b344021f-a496-4719-979e-a7c9167f6f54")
        Mockito.`when`(env.getProperty("keycloak.admin.user")).thenReturn("user-admin")
        Mockito.`when`(env.getProperty("keycloak.admin.password")).thenReturn("TriPa55w0rd")

        val signup = SignupDomain.Builder("newlight77+tu2@gmail.com")
                .firstname("Kong1")
                .lastname("To1")
                .phoneNumber("0600000000")
                .password("BonusMalus11")
                .build()

        // act
        val response = KeycloakRegistrationService(this.env).register(signup)

        // assert
        assertTrue(response)
    }

    // @Test
    fun `should update the password for a user on Keycloak`() {
        // arrange
        Mockito.`when`(env.getProperty("keycloak.base-url")).thenReturn("http://localhost:1080")
        Mockito.`when`(env.getProperty("keycloak.app.realm")).thenReturn("local.app")
        Mockito.`when`(env.getProperty("keycloak.admin.realm")).thenReturn("master")
        Mockito.`when`(env.getProperty("keycloak.admin.client-id")).thenReturn("admin-cli")
        Mockito.`when`(env.getProperty("keycloak.admin.client-secret")).thenReturn("753b3321-14bf-40be-9737-908e7676192c")
        Mockito.`when`(env.getProperty("keycloak.admin.user")).thenReturn("user-admin")
        Mockito.`when`(env.getProperty("keycloak.admin.password")).thenReturn("TriPa55w0rd")

        val username = "newlight77+tu3@gmail.com"
        val signup = SignupDomain.Builder(username)
            .firstname("Kong1")
            .lastname("To1")
            .phoneNumber("0600000000")
            .password("BonusMalus11")
            .build()

        KeycloakRegistrationService(this.env).register(signup)

        // act
        val result = KeycloakRegistrationService(this.env).updatePassword(username, "newpassword")

        // assert
        assertTrue(result)
    }

}
