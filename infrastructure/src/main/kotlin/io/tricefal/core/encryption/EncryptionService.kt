package io.tricefal.core.encryption

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.tricefal.core.okta.BcryptPassword
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.security.Key
import java.time.Instant
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter


@Service
class  EncryptionService(val encryptionProperties: EncrpyptionProperties) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun encode(id: String, subject: String, ttlMillis: Long): String {

        //The JWT signature algorithm we will be using to sign the token
        val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS256
        val apiKeySecretBytes: ByteArray = DatatypeConverter.parseBase64Binary(encryptionProperties.secretKey)
        val signingKey: Key = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName())

        val builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(Date())
                .setSubject(subject)
                .signWith(signingKey, signatureAlgorithm)
                .setExpiration(Date(Instant.now().toEpochMilli() + ttlMillis))

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact()
    }

    fun decode(token: String): String {
        //This line will throw an exception if it is not a signed JWS (as expected)
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(encryptionProperties.secretKey))
                .parseClaimsJws(token)
                .body.subject
    }

}