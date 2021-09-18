package io.oneprofile.core.user

import org.slf4j.LoggerFactory

class UserService(private var dataAdapter: UserDataAdapter) : IUserService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun updatePassword(username: String, newPassword: String): Boolean {
        try {
            if (newPassword.isNotBlank()) {
                return dataAdapter.updatePassword(username, newPassword)
            }
        } catch (ex: Exception) {
            logger.error("failed to update the password for username $username", ex)
            throw UserPasswordUpdateException("failed to update the password for username $username", ex)
        }
        return false
    }

}

class UserPasswordUpdateException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
