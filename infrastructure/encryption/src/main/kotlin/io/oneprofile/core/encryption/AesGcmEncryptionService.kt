package io.oneprofile.core.encryption

import org.springframework.stereotype.Service
import java.nio.charset.Charset
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

@Service
class AesGcmEncryptionService {

    // should be in a property
    val key = "gm6478soguasd7rpxbngiasog83skjhf".toByteArray(Charset.forName("UTF-8"))
    val nonce = generateKey(96)

    final fun generateKey(sizeInBits: Int): ByteArray {
        val result = ByteArray(sizeInBits / 8)
        SecureRandom().nextBytes(result)
        return result
    }

    fun encrypt(plaintext: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val keySpec = SecretKeySpec(key, "AES")

        val gcmSpec = GCMParameterSpec(128, nonce) // 128 bit authentication tag

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)

        return cipher.doFinal(plaintext)
    }

    /**
     * Decrypts the given [ciphertext] using the given [key] under AES GCM.
     *
     * @return Plaintext
     */
    fun decrypt(ciphertext: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val keySpec = SecretKeySpec(key, "AES")

        val gcmSpec = GCMParameterSpec(128, nonce) // 128 bit authentication tag

        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)

        return cipher.doFinal(ciphertext)
    }
}
