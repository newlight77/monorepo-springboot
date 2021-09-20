package io.oneprofile.signup.user

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class UserWebHandler(val userService: IUserService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun updatePassword(username: String, newPassword: String): Boolean {
        return this.userService.updatePassword(username, newPassword)
    }
}
