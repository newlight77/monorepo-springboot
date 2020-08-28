package io.tricefal.core.keycloak

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
//import javax.net.ssl.HttpsURLConnection


internal class KeycloakClientTest {

    private val client = KeycloakClient.Builder("http://localhost:1080")
            .build()

//    init {
//        // Setup the trustStore location and password
//        System.setProperty("javax.net.ssl.trustStore", "/Users/kong/wks/src/tricefal/tricefal-core/config/keycloak/certs/localhost/keycloak.localhost.truststore.jks")
//        System.setProperty("javax.net.ssl.trustStorePassword", "changit")
//        // comment out below line
//        System.setProperty("javax.net.ssl.keyStore","/Users/kong/wks/src/tricefal/tricefal-core/config/keycloak/certs/localhost/keycloak.localhost.keystore.jks")
//        System.setProperty("javax.net.ssl.keyStorePassword", "changeit")
//        System.setProperty("javax.net.debug", "all")
//
//        // for localhost testing only
//        HttpsURLConnection.setDefaultHostnameVerifier { hostname, sslSession -> hostname == "localhost" }
//    }

    // purposely ignore because this test is used to validate th integration with keycloak
    // @Test
    fun `should retrieve access token when using password grant`() {
        // arrange
        val grantType = "password"
        val clientId = "dev.tricefal.backend-confidential"
        val clientSecret = "2964cce2-e875-4c22-92ae-225d544d3571"
        val username = "newlight77+test1@gmail.com"
        val password = "TricefalWelyne1"

        // act
        val tokenResponse =
                this.client.login("dev.tricefal.io",
                        grantType = grantType,
                        clientId = clientId, clientSecret = clientSecret,
                        username = username, password = password)
                        .execute()

        // assert
        assertTrue(tokenResponse.isSuccessful)
        assertEquals(200, tokenResponse.code())
        tokenResponse.body()?.accessToken?.isNotEmpty()?.let { assertTrue(it) }
    }

    // purposely ignore because this test is used to validate th integration with keycloak
    // @Test
    fun `should retrieve access token when using client credentials`() {
        // arrange
        val grantType = "client_credentials"
//        val clientId = "account"
//        val clientSecret = "2969ee71-91c4-4c57-b990-440eeaee53f0"

        val clientId = "dev.tricefal.backend-confidential"
        val clientSecret = "2964cce2-e875-4c22-92ae-225d544d3571"

        // act
        val tokenResponse =
                this.client.clientToken("dev.tricefal.io",
                        grantType = grantType,
                        clientId = clientId,
                        clientSecret = clientSecret)
                    .execute()

        // assert
        assertTrue(tokenResponse.isSuccessful)
        assertEquals(200, tokenResponse.code())
        tokenResponse.body()?.accessToken?.isNotEmpty()?.let { assertTrue(it) }
    }

    // purposely ignore because this test is used to validate th integration with keycloak
    // @Test
    fun `should create a user on Keycloak`() {
        // arrange
        val grantType = "password"
        val clientId = "admin-cli"
        val clientSecret = "b344021f-a496-4719-979e-a7c9167f6f54"
        val username = "user-admin"
        val password = "TriPa55w0rd"

        val newUser = KeycloakNewUser.Builder()
                .firstName("Kong1")
                .lastName("To1")
                .username("newlight77+ut4@gmail.com")
//                .password("BonusMalus11")
                .build()

        val token =
                this.client.login("master",
                        grantType = grantType,
                        clientId = clientId, clientSecret = clientSecret,
                        username = username, password = password)
                        .execute().body()?.accessToken

        // act
        val tokenResponse =
                this.client.createUser(
                        realm = "dev.tricefal.io",
                        bearerToken = "Bearer $token",
                        user = newUser)
                    .execute()

        // assert
        assertTrue(tokenResponse.isSuccessful)
        assertEquals(201, tokenResponse.code())
    }

}
