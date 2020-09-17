package io.tricefal.core.keycloak

import io.tricefal.core.okta.IamRegisterService
import io.tricefal.core.right.AccessRight
import io.tricefal.core.signup.SignupDomain
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
            .realm(adminRealm) //
            .grantType(OAuth2Constants.PASSWORD) //
            .clientId(adminClientId) //
            .clientSecret(adminClientSecret) //
            .username(adminUser) //
            .password(adminPassword) //
            .build()

    private final val realmResource: RealmResource = keycloak.realm(appRealm)
    val usersResource: UsersResource = realmResource.users()
    var rolesResource: RolesResource = realmResource.roles()

    override fun register(signup: SignupDomain): Boolean {
        val user = toKeycloakUser(signup)

        val response = usersResource.create(user)
        logger.info("Response: ${response.location} ${response.status} ${response.statusInfo}")
        val userId = CreatedResponseUtil.getCreatedId(response)
        logger.info("User created with userId: $userId")

        if (response.status != 200 or 201) {
            logger.error("registration failed with : ${response.statusInfo}")
            throw KeycloakUnsuccessfulException("the response code is: " + response.statusInfo)
        }

        val userResource = usersResource[userId]
        val userCredential = toKeycloakCredential(signup)
        userResource.resetPassword(userCredential)

        return true
    }

    override fun addRole(username: String, role: AccessRight): Boolean {
        val testerRealmRole = rolesResource[role.label].toRepresentation()
        val userId = realmResource.users().search(username).last().id
        val userResource = usersResource[userId]
        userResource.roles().realmLevel().add(listOf(testerRealmRole))

        return true
    }

    private fun toKeycloakCredential(signup: SignupDomain): CredentialRepresentation {
        val passwordCred = CredentialRepresentation()
        passwordCred.isTemporary = false
        passwordCred.type = CredentialRepresentation.PASSWORD
        passwordCred.value = signup.password
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

