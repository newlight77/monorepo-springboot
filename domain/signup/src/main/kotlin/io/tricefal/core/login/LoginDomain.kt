package io.tricefal.core.login

import java.time.Instant
import java.util.*

const val EMAIL_REGEX = " ^[\\\\w!#\$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}\$"

data class LoginDomain(
        var username: String,
        val loginDate: Instant? = null,
        val ipAddress: String? = null,
        val city: String? = null,
        val region: String? = null,
        val device: String? = null,
        val success: Boolean? = false
)
