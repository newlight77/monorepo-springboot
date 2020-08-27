package io.tricefal.core.keycloak

import com.google.gson.Gson
import io.tricefal.core.signup.SignupDomain
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service
@PropertySource("classpath:keycloak.yml")
class KeycloakAccountService(private val env: Environment) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val baseUrl = env.getProperty("keycloak.base-url")
    private val realm = env.getProperty("keycloak.account.realm")
    private val clientId = env.getProperty("keycloak.account.client-id")
    private val clientSecret = env.getProperty("keycloak.account.client-secret")

    val keycloakClient: KeycloakClient = KeycloakClient
            .Builder(baseUrl!!)
            .build()

    fun register(signup: SignupDomain): Boolean {
        val token = this.clientToken().accessToken
        val user = toKeycloakNewUser(signup)
        val response = keycloakClient.createUser(this.realm!!, "Bearer $token", user)
        val result = response.execute()

        print(Gson().toJson(user))

        logger.info("keycloak code : ${result.code()}")
        logger.info("keycloak raw : ${result.raw()}")

        if (result.code() != 200 and 201) {
            logger.error("registration failed with : ${result.raw()}")
            throw KeycloakUnsuccessfulException("the response code is: " + result.raw())
        }

        return "201" == result.body()?.code
    }

    private fun clientToken(): KeycloakTokenResponse {

        val result =
                this.keycloakClient.clientToken(
                        realm =this.realm!!,
                        grantType = "client_credentials",
                        clientId = this.clientId!!, clientSecret = this.clientSecret!!)
                        .execute()

        logger.info("keycloak body : ${result.body()}")

        return result.body()!!
    }

}

