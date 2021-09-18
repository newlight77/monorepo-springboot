package io.oneprofile.core.user

import io.oneprofile.core.keycloak.KeycloakRegistrationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserRepositoryAdapter(
    private var registrationService: KeycloakRegistrationService,
) : UserDataAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun updatePassword(username: String, newPassword: String): Boolean {
        return try {
            val result = registrationService.updatePassword(username, newPassword)
            result
        } catch (ex: Exception) {
            logger.error("Failed to update the password on IAM server for username $username", ex)
            throw UserPasswordUpdateException("Failed to update the password on IAM server for username $username", ex)
        }
    }

    class UserPasswordUpdateException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
}





