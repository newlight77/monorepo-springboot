package io.tricefal.core.login

import java.time.Instant
import java.util.*

const val LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$"
const val EMAIL_REGEX = " ^[\\\\w!#\$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}\$"

data class LoginDomain(
        var id: Long,
        var username: String,
        val lastLogin: Instant = Instant.now(),
        val ipAddress: String,
        val success: Boolean = false,
        val authorities: Set<String> = HashSet()
)
