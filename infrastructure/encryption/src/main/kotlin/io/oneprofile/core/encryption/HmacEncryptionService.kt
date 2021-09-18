package io.oneprofile.core.encryption

import org.springframework.stereotype.Service
import java.security.SecureRandom
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Service
class HmacEncryptionService {

    fun generateKey(sizeInBits: Int): ByteArray {
        val result = ByteArray(sizeInBits / 8)
        SecureRandom().nextBytes(result)
        return result
    }

    fun createHmacSha256(data: ByteArray, key: ByteArray): ByteArray {
        val keySpec = SecretKeySpec(key, "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(keySpec)

        val hmac = mac.doFinal(data)
        return hmac
    }

    /**
     * The HMAC comparison is done in a timing attack proof way.
     */
    fun checkHmac(data: ByteArray, key: ByteArray, expectedHmac: ByteArray): Boolean {
        val hmac = createHmacSha256(data, key)

        // Check for equality in a timing attack proof way
        if (hmac.size != expectedHmac.size) return false
        var result = 0
        for (i in 0 until hmac.size) {
            result = result.or(hmac[i].toInt().xor(expectedHmac[i].toInt()))
        }

        return result == 0
    }

}