package io.oneprofile.signup.encryption

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.security.Key
import java.time.Instant
import java.util.*
import javax.crypto.spec.SecretKeySpec
import java.util.Base64


@Service
@PropertySource("encryption.yml")
class  JwtEncryptionService(private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private var secretKey = env.getProperty("encryption.secretKey")!!

    fun encode(id: String, subject: String, ttlMillis: Long): String {

        //The JWT signature algorithm we will be using to sign the token
        val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS256
        val apiKeySecretBytes: ByteArray = Base64.getDecoder().decode(secretKey)
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
        return Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode(secretKey))
                .build()
                .parseClaimsJws(token).body.subject
    }

}