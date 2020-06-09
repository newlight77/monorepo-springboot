package io.tricefal.core.encryption

import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SecurityException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EncryptionServiceTest {

    private val encrpyptionProperties = EncrpyptionProperties("oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGh")
    private val encryptionService = EncryptionService(encrpyptionProperties)

    @Test
    fun createAndDecodeJWT() {

        // arrange
        val jwtId = "SOMEID1234"
        val jwtSubject = "Andrew"
        val jwtTimeToLive = 800000L
        val jwt: String = encryptionService.encode(
                jwtId,
                jwtSubject,
                jwtTimeToLive
        )

        // act
        val decoded = encryptionService.decode(jwt)

        // assert
        assertEquals(jwtSubject, decoded)
    }

    @Test
    fun decodeShouldFail() {

        // arrange
        val notAJwt = "This is not a a valid token"

        // act
        val exception = Assertions.assertThrows(
                MalformedJwtException::class.java
        ) { encryptionService.decode(notAJwt) }

        // assert
        assertEquals("JWT strings must contain exactly 2 period characters. Found: 0", exception.message)
    }

    @Test
    fun createAndDecodeTamperedJWT() {

        // arrange
        val jwtId = "SOMEID1234"
        val jwtSubject = "Andrew"
        val jwtTimeToLive = 800000L

        val jwt = encryptionService.encode(jwtId, jwtSubject, jwtTimeToLive)

        val tamperedJwt = StringBuilder(jwt)
        tamperedJwt.setCharAt(22, 'I')

        // act
        val exception = Assertions.assertThrows(
                SecurityException::class.java
        ) { encryptionService.decode(tamperedJwt.toString()) }

        // assert
        Assertions.assertTrue(exception.message?.contains("JWT signature does not match locally computed signature.")!!)
    }

}