package io.tricefal.core.security

import com.okta.jwt.Jwt
import com.okta.jwt.JwtVerifiers
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Duration

@Component
@EnableConfigurationProperties(OAuthProperties::class)
class OktaJwtVerifier {
    val logger = LogFactory.getLog(javaClass)

    @Autowired
    lateinit var oAuthProperties: OAuthProperties

    fun decode(jwtString: String): Jwt {
        logger.info("securityProperties : $oAuthProperties")
        val accessTokenVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                .setIssuer(oAuthProperties.issuer)
                .setConnectionTimeout(Duration.ofSeconds(1)) // defaults to 1000ms
                .setReadTimeout(Duration.ofSeconds(1)) // defaults to 1000ms
                .build()
        return accessTokenVerifier.decode(jwtString)
    }
}

