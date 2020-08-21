package io.tricefal.core.keycloak

import com.google.gson.Gson
import io.tricefal.core.signup.SignupDomain
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service
@PropertySource("classpath:keycloak.yml")
class KeycloakService(private val env: Environment) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val baseUrl = env.getProperty("keycloak.baseUrl")
    private val realm = env.getProperty("keycloak.realm")
    private val clientId = env.getProperty("keycloak.clientId")
    private val clientSecret = env.getProperty("keycloak.clientSecret")
    private val grantType = env.getProperty("keycloak.grantType")

    private val keycloakClient: KeycloakClient = KeycloakClient
            .Builder(baseUrl!!, realm!!)
            .grantType(grantType!!)
            .clientId(clientId!!)
            .clientSecret(clientSecret!!)
            .build()

    private fun login(signup: SignupDomain): KeycloakTokenResponse {
        val user = toKeycloakPasswordGrantType(signup)

        val keycloakClient: KeycloakClient = KeycloakClient
                .Builder(baseUrl!!, realm!!)
                .grantType(grantType!!)
                .clientId(clientId!!)
                .clientSecret(clientSecret!!)
                .build()

        val call = keycloakClient.login(this.realm, user)
        val result = call.execute()

        print(Gson().toJson(user))

        logger.info("keycloak body : ${result.body()}")

        return result.body()!!
    }

    fun register(signup: SignupDomain): Boolean {
        val user = toKeycloakNewUser(signup)

        val token = this.login(signup).accessToken

        val keycloakClient: KeycloakClient = KeycloakClient
                .Builder(baseUrl!!, realm!!)
                .token(token)
                .grantType(grantType!!)
                .clientId(clientId!!)
                .clientSecret(clientSecret!!)
                .build()

        val response = keycloakClient.createUser(realm, user)
        val result = response.execute()

        print(Gson().toJson(user))

        logger.info("keycloak code : ${result.code()}")
        logger.info("keycloak raw : ${result.raw()}")

        if (result.code() != 200 and 201) {
            logger.error("registration failed with : ${result.raw()}")
            throw KeycloakUnsuccessfulRegistration("the response code is: " + result.raw())
        }

        return "201" == result.body()?.code
    }

}

class KeycloakUnsuccessfulRegistration(val msg: String) : Throwable(msg) {
}
