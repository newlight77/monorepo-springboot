package io.tricefal.core.profile

import io.tricefal.core.signup.SignupState
import io.tricefal.core.signup.Status
import java.time.Instant

data class ProfileDomain(
    var username: String,
    var firstname: String?,
    var lastname: String?,
    var phoneNumber: String?,

    var status: Status? = Status.NONE,
    var signupState: SignupState? = SignupState.NONE,
    var lastDate: Instant? = null,

    var portraitFilename: String? = null,
    var resumeFilename: String? = null,
    var resumeLinkedinFilename: String? = null
    ) {

    data class Builder(
            val username: String,

            var firstname: String? = null,
            var lastname: String? = null,
            var phoneNumber: String? = null,

            var status: Status? = Status.NONE,
            var signupState: SignupState? = SignupState.NONE,
            var lastDate: Instant? = null,

            var portraitFilename: String? = null,
            var resumeFilename: String? = null,
            var resumeLinkedinFilename: String? = null
    ) {
        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }

        fun status(status: Status?) = apply { this.status = status }
        fun signupState(signupState: SignupState?) = apply { this.signupState = signupState }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun portraitFilename(portraitFilename: String?) = apply { this.portraitFilename = portraitFilename }
        fun resumeFilename(resumeFilename: String?) = apply { this.resumeFilename = resumeFilename }
        fun resumeLinkedinFilename(resumeLinkedinFilename: String?) = apply { this.resumeLinkedinFilename = resumeLinkedinFilename }

        fun build() = ProfileDomain(
            username = username,
            firstname = username,
            lastname = username,
            phoneNumber = username,
            status = status,
            signupState = signupState,
            lastDate = lastDate,
            portraitFilename = portraitFilename,
            resumeFilename = resumeFilename,
            resumeLinkedinFilename = resumeLinkedinFilename
        )
    }
}
