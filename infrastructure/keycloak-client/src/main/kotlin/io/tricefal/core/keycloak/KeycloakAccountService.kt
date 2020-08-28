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
    private val appRealm = env.getProperty("keycloak.app.realm")
    private val adminRealm = env.getProperty("keycloak.admin.realm")
    private val adminClientId = env.getProperty("keycloak.admin.client-id")
    private val adminClientSecret = env.getProperty("keycloak.admin.client-secret")
    private val adminUser = env.getProperty("keycloak.admin.user")
    private val adminPassword = env.getProperty("keycloak.admin.password")

    val keycloakClient: KeycloakClient = KeycloakClient
            .Builder(baseUrl!!)
            .build()

    fun register(signup: SignupDomain): Boolean {
        val token = this.adminToken().accessToken
        val user = toKeycloakNewUser(signup)
        val response = keycloakClient.createUser(this.appRealm!!, "Bearer $token", user)
        val result = response.execute()

        print(Gson().toJson(user))

        logger.info("keycloak code : ${result.code()}")
        logger.info("keycloak raw : ${result.raw()}")

        if (result.code() != 200 or 201) {
            logger.error("registration failed with : ${result.raw()}")
            throw KeycloakUnsuccessfulException("the response code is: " + result.raw())
        }

        return result.isSuccessful
    }

    private fun adminToken(): KeycloakTokenResponse {

        val result =
                this.keycloakClient.login(
                        realm = this.adminRealm!!,
                        grantType = "password",
                        username = this.adminUser!!, password = this.adminPassword!!,
                        clientId = this.adminClientId!!, clientSecret = this.adminClientSecret!!)
                        .execute()

        logger.info("keycloak body : ${result.body()}")

        return result.body()!!
    }

}

