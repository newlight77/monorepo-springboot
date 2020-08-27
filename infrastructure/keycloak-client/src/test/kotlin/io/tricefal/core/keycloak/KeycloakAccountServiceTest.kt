package io.tricefal.core.keycloak

import io.tricefal.core.signup.SignupDomain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.core.env.Environment

internal class KeycloakAccountServiceTest {

    @Mock
    val env: Environment = Mockito.mock(Environment::class.java)

    @Test
    fun `should create a user on Keycloak`() {
        // arrange
        Mockito.`when`(env.getProperty("keycloak.base-url")).thenReturn("http://localhost:1080")
        Mockito.`when`(env.getProperty("keycloak.realm")).thenReturn("master")
        Mockito.`when`(env.getProperty("keycloak.account.client-id")).thenReturn("account")
        Mockito.`when`(env.getProperty("keycloak.account.client-secret")).thenReturn("2969ee71-91c4-4c57-b990-440eeaee53f0")

        val signup = SignupDomain.Builder("newlight77@gmail.com")
                .firstname("Kong1")
                .lastname("To1")
                .phoneNumber("0600000000")
                .password("BonusMalus11")
                .build()

        // act
        val response = KeycloakAccountService(this.env).register(signup)

        // assert
        assertEquals(22, response)
    }

}
