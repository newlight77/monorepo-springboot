package io.oneprofile.signup.encryption

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HmacEncryptionServiceTest {

    private val service = HmacEncryptionService()

    @Test fun testAesCbcAndHmac() {
        // Arrange
        val hmacKey = service.generateKey(256)
        val plaintext = "This is the HMAC test"

        // Act
        val hmac = service.createHmacSha256(plaintext.toByteArray(), hmacKey)
        val result = service.checkHmac(plaintext.toByteArray(), hmacKey, hmac)

        // Assert
        Assertions.assertTrue(result)
    }

}