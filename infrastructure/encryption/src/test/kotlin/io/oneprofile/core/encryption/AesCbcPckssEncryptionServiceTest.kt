package io.oneprofile.core.encryption

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AesCbcPckssEncryptionServiceTest {

    private val service = AesCbcPckssEncryptionService()
    private val hmacService = HmacEncryptionService()

    @Test fun `shoud encrypt then decrypt a plain text successfully`() {
        // Arrange
        val cbcKey = service.generateKey(256)

        val plaintext = "This is the CBC test"

        // Act
        val ciphertext = service.encryptAesCbcPkcss(plaintext.toByteArray(), cbcKey)
        val decrypted = String(service.decryptAesCbcPkcss(ciphertext, cbcKey), Charsets.UTF_8)

        // Assert
        Assertions.assertEquals(plaintext, decrypted)
    }

    @Test fun `shoud encrypt then decrypt a plain text successfully with hmac verification`() {
        val cbcKey = service.generateKey(256)
        val hmacKey = service.generateKey(256)

        val plaintext = "This is the CBC test"

        val ciphertext = service.encryptAesCbcPkcss(plaintext.toByteArray(), cbcKey)
        val hmac = hmacService.createHmacSha256(ciphertext.iv + ciphertext.ciphertext, hmacKey)

        // Here the ciphertext and the HMAC should be stored somewhere.
        // It should not contain any secret information.

        // Before decrypting, check the HMAC. If it doesn't match, someone has tampered the data!
        if (!hmacService.checkHmac(ciphertext.iv + ciphertext.ciphertext, hmacKey, hmac)) throw IllegalStateException("HMAC failed")

        val decrypted = String(service.decryptAesCbcPkcss(ciphertext, cbcKey), Charsets.UTF_8)

        Assertions.assertEquals(plaintext, decrypted)
    }

}