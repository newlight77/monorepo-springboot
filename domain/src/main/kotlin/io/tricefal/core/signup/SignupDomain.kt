package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import java.time.Instant
import java.util.*

data class SignupDomain(
        var id: Long,
        var username: String,
        val firstname: String,
        val lastname: String,
        val phoneNumber: String,
        val signupDate: Instant = Instant.now(),

        var activationcode: String,
        var status: Status?,
        var metafile: MetafileDomain? = null
)

enum class Status {
    FREELANCE,
    EMPLOYEE
}
