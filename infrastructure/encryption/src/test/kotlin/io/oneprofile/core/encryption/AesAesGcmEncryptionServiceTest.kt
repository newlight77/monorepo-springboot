package io.oneprofile.core.encryption

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AesAesGcmEncryptionServiceTest {

    private val service = AesGcmEncryptionService()

    @Test
    fun `should encrypt and decrypt correctly using AES GCM`() {
        // Arrange
        val plaintext = "This is GCM"

        // Act
        val ciphertext = service.encrypt(plaintext.toByteArray())
        val decrypted = String(service.decrypt(ciphertext), Charsets.UTF_8)

        // Assert
        Assertions.assertEquals(plaintext, decrypted)
    }

}