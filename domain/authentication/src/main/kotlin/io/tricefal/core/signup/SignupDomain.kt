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
            var activationCode: String?,
            var activationToken: String?,
            var status: Status?,
            var cguAcceptedVersion: String?,
            val signupDate: Instant?,

            var portraitFile: MetafileDomain? = null,
            var resumeFile: MetafileDomain? = null,
            var resumeLinkedinFile: MetafileDomain? = null,
            var state: SignupStateDomain? = null
    ) {

    data class Builder(
            val username: String,
            var password: String? = null,
            var firstname: String? = null,
            var lastname: String? = null,
            var phoneNumber: String? = null,
            var activationCode: String? = null,
            var activationToken: String? = null,
            var status: Status? = null,
            var cguAcceptedVersion: String? = null,
            var signupDate: Instant? = null,

            var portraitFile: MetafileDomain? = null,
            var resumeFile: MetafileDomain? = null,
            var resumeLinkedinFile: MetafileDomain? = null,

            var state: SignupStateDomain? = null
    ) {
        fun password(password: String?) = apply { this.password = password }
        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
        fun activationCode(activationCode: String?) = apply { this.activationCode = activationCode }
        fun activationToken(activationToken: String?) = apply { this.activationToken = activationToken }
        fun status(status: Status) = apply { this.status = status }
        fun cguAcceptedVersion(cguAcceptedVersion: String?) = apply { this.cguAcceptedVersion = cguAcceptedVersion }
        fun signupDate(signupDate: Instant?) = apply { this.signupDate = signupDate }
        fun portraitFile(portraitFile: MetafileDomain?) = apply { this.portraitFile = portraitFile }
        fun resumeFile(resumeFile: MetafileDomain?) = apply { this.resumeFile = resumeFile }
        fun resumeLinkedinFile(resumeLinkedinFile: MetafileDomain?) = apply { this.resumeLinkedinFile = resumeLinkedinFile }

        fun state(state: SignupStateDomain?) = apply {
            this.state = state ?: SignupStateDomain.Builder(username).build()
        }

        fun build() = SignupDomain(
                username = username,
                password = password,
                firstname = firstname,
                lastname = lastname,
                phoneNumber = phoneNumber,
                activationCode = activationCode,
                activationToken = activationToken,
                status = status,
                cguAcceptedVersion = cguAcceptedVersion,
                signupDate = signupDate,
                portraitFile = portraitFile,
                resumeFile = resumeFile,
                resumeLinkedinFile = resumeLinkedinFile,
                state = state
        )
    }
}

enum class Status {
    NONE,
    CLIENT,
    FREELANCE,
    EMPLOYEE;
}

fun toStatus(status: String): Status {
    try {
        return Status.valueOf(status.toUpperCase())
    } catch (ex: Exception) {
        throw IllegalArgumentException("invalid argument : status=$status")
    }
}