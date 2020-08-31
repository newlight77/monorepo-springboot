package io.tricefal.core.security

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.okta.jwt.Jwt
import com.okta.jwt.JwtVerifiers
import org.apache.commons.logging.LogFactory
import org.checkerframework.checker.nullness.qual.Nullable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.TimeUnit

@Component
@EnableConfigurationProperties(OAuthProperties::class)
class OktaJwtVerifier {
    val logger = LogFactory.getLog(javaClass)

    @Autowired
    lateinit var oAuthProperties: OAuthProperties

    var jwtCache: Cache<String, Jwt> = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .refreshAfterWrite(2, TimeUnit.MINUTES)
            .build { this.decode(it) }

    fun getOrDecode(jwtString: String) : @Nullable Jwt? {
        return jwtCache.get(jwtString, this::decode)
    }

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

