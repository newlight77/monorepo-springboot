package io.tricefal.core.keycloak

import io.tricefal.core.signup.SignupDomain
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service
@PropertySource("classpath:keycloak.yml")
class KeycloakLoginService(private val env: Environment) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val baseUrl = env.getProperty("keycloak.base-url")
    private val realm = env.getProperty("keycloak.app.realm")
    private val clientId = env.getProperty("keycloak.app.client-id")
    private val clientSecret = env.getProperty("keycloak.app.client-secret")

    val keycloakClient: KeycloakClient = KeycloakClient
            .Builder(baseUrl!!)
            .build()

    private fun login(signup: SignupDomain): KeycloakTokenResponse {
        val result =
                this.keycloakClient.login(
                    realm =this.realm!!,
                    grantType = "password",
                    username = signup.username, password = signup.password!!,
                    clientId = this.clientId!!, clientSecret = this.clientSecret!!)
                .execute()

        logger.info("keycloak body : ${result.body()}")

        return result.body()!!
    }

}
