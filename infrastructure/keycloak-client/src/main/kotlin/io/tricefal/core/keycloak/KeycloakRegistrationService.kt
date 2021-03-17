package io.tricefal.core.keycloak

import io.tricefal.core.right.AccessRight
import io.tricefal.core.signup.SignupDomain
import org.jboss.resteasy.client.jaxrs.ResteasyClient
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.RolesResource
import org.keycloak.admin.client.resource.UsersResource
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.util.*
import javax.ws.rs.core.Response


@Service
@PropertySource("classpath:keycloak.yml")
class KeycloakRegistrationService(private val env: Environment): IamRegisterService {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val baseUrl = env.getProperty("keycloak.base-url")
    private val appRealm = env.getProperty("keycloak.app.realm")
    private val adminRealm = env.getProperty("keycloak.admin.realm")
    private val adminClientId = env.getProperty("keycloak.admin.client-id")
    private val adminClientSecret = env.getProperty("keycloak.admin.client-secret")
    private val adminUser = env.getProperty("keycloak.admin.user")
    private val adminPassword = env.getProperty("keycloak.admin.password")

    final var keycloak: Keycloak = KeycloakBuilder.builder() //
            .serverUrl("${baseUrl}/auth") //
            .resteasyClient(resteasyClient())
            .realm(adminRealm) //
            .grantType(OAuth2Constants.PASSWORD) //
            .clientId(adminClientId) //
            .clientSecret(adminClientSecret) //
            .username(adminUser) //
            .password(adminPassword) //
            .build()

    private final val realmResource: RealmResource = keycloak.realm(appRealm)
    val realmUsersResource: UsersResource = realmResource.users()
    var realmRolesResource: RolesResource = realmResource.roles()

    private fun resteasyClient(): ResteasyClient? {
        val clientBuilder = ResteasyClientBuilder().disableTrustManager()
        clientBuilder.setIsTrustSelfSignedCertificates(true)
        return clientBuilder.build()
    }
    
    override fun register(signup: SignupDomain): Boolean {
        val user = toKeycloakUser(signup)

        val response = createUser(user)
        logger.info("Response: ${response.location} ${response.status} ${response.statusInfo}")
        val userId = getCreatedId(response)
        val userCredential = toKeycloakCredential(signup.username)

        try {
            realmUsersResource[userId].resetPassword(userCredential)
        } catch (ex: Exception) {
            logger.error("update password failed for user ${signup.username} in realm $appRealm with exception: ${ex.localizedMessage}", ex)
            throw KeycloakUnsuccessfulException("update password failed for user ${signup.username} in realm $appRealm with exception: ${ex.stackTrace}", ex)
        }

        return true
    }

    override fun delete(username: String): Boolean {
        val response = try {
            realmUsersResource.delete(username)
        } catch (ex: Exception) {
            logger.error("delete keycloak account failed in realm $appRealm with exception: ${ex.localizedMessage}", ex)
            throw KeycloakUnsuccessfulException("delete keycloak account failed in realm $appRealm with exception: ${ex.stackTrace}", ex)
        }

        if (response.status != 200 or 204) {
            logger.error("deletion of account failed with : ${response.statusInfo}")
            throw KeycloakUnsuccessfulException("the response code is: " + response.statusInfo)
        }

        return true
    }

    override fun updatePassword(username: String, password: String): Boolean {

        val users = find(username)
        if (users.isNotEmpty()) {
            val user: UserRepresentation = users.last()
            val credential = toKeycloakCredential(username)

            try {
                realmUsersResource[user.id].resetPassword(credential)
            } catch (ex: Exception) {
                logger.error("update password failed for user $username in realm $appRealm with exception: ${ex.localizedMessage}", ex)
                throw KeycloakUnsuccessfulException("update password failed for user $username in realm $appRealm with exception: ${ex.stackTrace}", ex)
            }
        }

        return true
    }

    override fun addRole(username: String, role: AccessRight): Boolean {
        val realmRole = realmRolesResource[role.label].toRepresentation()

        val users = find(username)
        val userId = users.last().id
        val userResource = realmUsersResource[userId]
        userResource.roles().realmLevel().add(listOf(realmRole))

        userResource.update(users.last())

        try {
            userResource.update(users.last())
        } catch (ex: Exception) {
            logger.error("registration failed while updating the user role in realm $appRealm with exception: ${ex.localizedMessage}", ex)
            throw KeycloakUnsuccessfulException("registration failed while updating the user role in realm $appRealm with exception: ${ex.stackTrace}", ex)
        }

        return true
    }

    private fun getCreatedId(response: Response): String {
        val userId = try {
            CreatedResponseUtil.getCreatedId(response)
        } catch (ex: Exception) {
            logger.error("failed to retrieve the created id for the created user in realm $appRealm with exception: ${ex.localizedMessage}", ex)
            throw KeycloakUnsuccessfulException("failed to retrieve the created id for the created user inrealm $appRealm with exception: ${ex.localizedMessage}", ex)
        }

        if (response.status != 200 or 201) {
            logger.error("registration failed with : ${response.statusInfo}")
            throw KeycloakUnsuccessfulException("the response code is: " + response.statusInfo)
        }

        logger.info("User created with userId: $userId")

        return userId
    }

    private fun createUser(user: UserRepresentation): Response {
        return try {
            realmUsersResource.create(user)
        } catch (ex: Exception) {
            logger.error("registration failed while creating a user resource in realm $appRealm with exception: ${ex.localizedMessage}", ex)
            throw KeycloakUnsuccessfulException("registration failed while creating a user resource in realm $appRealm with exception: ${ex.stackTrace}", ex)
        }
    }

    private fun find(username: String): List<UserRepresentation> {
        return try {
            realmUsersResource.search(username)
        } catch (ex: Exception) {
            logger.error("Error occured while finding a user in realm $appRealm with exception: ${ex.localizedMessage}", ex)
            throw KeycloakUnsuccessfulException("Error occured while finding a user in realm $appRealm with exception: ${ex.stackTrace}", ex)
        }
    }

    private fun toKeycloakCredential(password: String): CredentialRepresentation {
        val passwordCred = CredentialRepresentation()
        passwordCred.isTemporary = false
        passwordCred.type = CredentialRepresentation.PASSWORD
        passwordCred.value = password
        return passwordCred
    }

    private fun toKeycloakUser(signup: SignupDomain): UserRepresentation {
        val user = UserRepresentation()
        user.isEnabled = true
        user.isEmailVerified = true
        user.username = signup.username
        user.firstName = signup.firstname
        user.lastName = signup.lastname
        user.email = signup.username
        user.attributes = Collections.singletonMap("origin", listOf("new user"))
        return user
    }

}

