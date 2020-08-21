package io.tricefal.core.okta

import com.google.gson.Gson
import io.tricefal.core.keycloak.KeycloakNewUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class KeycloakClientTest {

    @Test
    fun `should create a user on Okta platform`() {
        // arrange

        // act

        // assert
    }

    @Test
    fun `should generate a bcrypt salt and hash with proper length`() {
        // arrange
        val user = KeycloakNewUser.Builder()
                .username("newlight77+test1@gmail.com")
                .password("password123KKK")
                .firstName("test1")
                .lastName("test1")
                .mobilePhone("1234567890")
                .build()

        // act
        val json = Gson().toJson(user)
        print(json)

        // assert
        assertEquals(22, user.credentials.salt.length)
        assertEquals(31, user.credentials.hashedSaltedValue.length)
    }
}
