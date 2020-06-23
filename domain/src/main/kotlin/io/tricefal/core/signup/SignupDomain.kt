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
            var status: Status?,
            val signupDate: Instant?,

            var resumeFile: MetafileDomain? = null,
//            var notification: SignupNotificationDomain? = null,
            var state: SignupStateDomain? = null
    ) {

    data class Builder(
            val username: String,
            var password: String? = null,
            var firstname: String? = null,
            var lastname: String? = null,
            var phoneNumber: String? = null,
            var activationCode: String? = null,
            var status: Status? = null,
            var signupDate: Instant? = null,

            var resumeFile: MetafileDomain? = null,
//            var notification: SignupNotificationDomain? = null,
            var state: SignupStateDomain? = null
    ) {
        fun password(password: String?) = apply { this.password = password }
        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
        fun activationCode(activationCode: String?) = apply { this.activationCode = activationCode }
        fun status(status: Status) = apply { this.status = status }
        fun signupDate(signupDate: Instant?) = apply { this.signupDate = signupDate }
        fun resumeFile(resumeFile: MetafileDomain?) = apply { this.resumeFile = resumeFile }
//        fun notification(notification: SignupNotificationDomain?) = apply { this.notification = notification }
        fun state(state: SignupStateDomain?) = apply { this.state = state }

        fun build() = SignupDomain(username,
                password,
                firstname,
                lastname,
                phoneNumber,
                activationCode,
                status,
                signupDate,
                resumeFile,
//                notification,
                state
        )
    }
}

enum class Status {
    UNKNOWN,
    FREELANCE,
    EMPLOYEE
}
