package io.tricefal.core.login

import java.time.Instant
import java.util.*

const val EMAIL_REGEX = " ^[\\\\w!#\$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}\$"

data class LoginDomain(
        var username: String,
        val loginDate: Instant = Instant.now(),
        val ipAddress: String,
        val city: String,
        val region: String,
        val device: String,
        val success: Boolean = false,
        val authorities: Set<String> = HashSet()
)
