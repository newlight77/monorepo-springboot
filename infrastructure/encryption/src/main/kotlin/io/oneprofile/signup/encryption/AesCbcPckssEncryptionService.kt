package io.oneprofile.signup.encryption

import org.springframework.stereotype.Service
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Service
class AesCbcPckssEncryptionService {

    fun generateKey(sizeInBits: Int): ByteArray {
        val result = ByteArray(sizeInBits / 8)
        SecureRandom().nextBytes(result)
        return result
    }

    fun generateIv(): ByteArray {
        val result = ByteArray(128 / 8)
        SecureRandom().nextBytes(result)
        return result
    }

    class Ciphertext(val ciphertext: ByteArray, val iv: ByteArray)

    fun encryptAesCbcPkcss(plaintext: ByteArray, key: ByteArray): Ciphertext {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key, "AES")

        val iv = generateIv()
        val ivSpec = IvParameterSpec(iv)

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

        val ciphertext = cipher.doFinal(plaintext)

        return Ciphertext(ciphertext, iv)
    }

    fun decryptAesCbcPkcss(ciphertext: Ciphertext, key: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keySpec = SecretKeySpec(key, "AES")
        val ivSpec = IvParameterSpec(ciphertext.iv)

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

        val plaintext = cipher.doFinal(ciphertext.ciphertext)
        return plaintext
    }

}