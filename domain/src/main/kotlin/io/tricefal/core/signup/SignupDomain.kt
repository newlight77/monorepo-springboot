package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import java.time.Instant

data class SignupDomain
    constructor(
        val username: String,
        val password: String?,
        val firstname: String?,
        val lastname: String?,
        val phoneNumber: String?,
        val signupDate: Instant?,

        var activationCode: String?,
        var status: Status?,
        var metafile: MetafileDomain? = null) {

    data class Builder(
            val username: String,
            var password: String? = null,
            var firstname: String? = null,
            var lastname: String? = null,
            var phoneNumber: String? = null,
            var signupDate: Instant? = null,

            var activationCode: String? = null,
            var status: Status? = null,
            var metafile: MetafileDomain? = null
    ) {
        fun password(password: String?) = apply { this.password = password }
        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
        fun signupDate(signupDate: Instant?) = apply { this.signupDate = signupDate }
        fun activationCode(activationCode: String?) = apply { this.activationCode = activationCode }
        fun status(status: Status) = apply { this.status = status }
        fun metafile(metafile: MetafileDomain?) = apply { this.metafile = metafile }

        fun build() = SignupDomain(username, password, firstname, lastname,
                phoneNumber, signupDate, activationCode, status, metafile)
    }
}

enum class Status {
    UNKNOWN,
    FREELANCE,
    EMPLOYEE
}
