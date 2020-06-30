package io.tricefal.core.okta

import com.google.gson.Gson
import io.tricefal.core.okta.OktaUser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mindrot.jbcrypt.BCrypt

internal class OktaClientTest {

    @Test
    fun `should create a user on Okta platform`() {
        // arrange

        // act

        // assert
    }

    @Test
    fun `should generate a bcrypt salt and hash with proper length`() {
        // arrange
        val user = OktaUser.Builder()
                .email("newlight77+test1@gmail.com")
                .password("password123KKK")
                .firstName("test1")
                .lastName("test1")
                .mobilePhone("1234567890")
                .build()

        // act
        val json = Gson().toJson(user)
        print(json)

        // assert
        assertEquals(22, user.credentials.password.hash.salt.length)
        assertEquals(31, user.credentials.password.hash.value.length)
    }
}
