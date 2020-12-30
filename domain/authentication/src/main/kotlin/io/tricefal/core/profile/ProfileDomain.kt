package io.tricefal.core.profile

import io.tricefal.core.signup.Status
import java.time.Instant

data class ProfileDomain(
    var username: String,
    var status: Status? = Status.NONE,
    var signupState: SignupState? = SignupState.NONE,
    var lastDate: Instant? = Instant.now(),
    var portraitFilename: String? = null,
    var resumeFilename: String? = null,
    var resumeLinkedinFilename: String? = null
    ) {

    data class Builder(
            val username: String,
            var status: Status? = Status.NONE,
            var signupState: SignupState? = SignupState.NONE,
            var lastDate: Instant? = null,

            var portraitFilename: String? = null,
            var resumeFilename: String? = null,
            var resumeLinkedinFilename: String? = null
    ) {
        fun status(status: Status?) = apply { this.status = status }
        fun signupState(signupState: SignupState?) = apply { this.signupState = signupState }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }
        fun portraitFilename(portraitFilename: String?) = apply { this.portraitFilename = portraitFilename }
        fun resumeFilename(resumeFilename: String?) = apply { this.resumeFilename = resumeFilename }
        fun resumeLinkedinFilename(resumeLinkedinFilename: String?) = apply { this.resumeLinkedinFilename = resumeLinkedinFilename }

        fun build() = ProfileDomain(
            username = username,
            status = status,
            signupState = signupState,
            lastDate = lastDate,
            portraitFilename = portraitFilename,
            resumeFilename = resumeFilename,
            resumeLinkedinFilename = resumeLinkedinFilename
        )
    }
}

enum class SignupState {
    NONE,
    CGU_ACCEPTED,
    REGISTERED,
    SMS_CODE_SENT,
    SMS_CODE_VALIDATED,
    EMAIL_SENT,
    EMAIL_VALIDATED,
    PORTRAIT_UPLOADED, // optional
    RESUME_UPLOADED,
    RESUME_LINKEDIN_UPLOADED,
    STATUS_SET,
    VALIDATED, // activated by backoffice
    MISSION_FORM_FILLED,
    ENTERPRISE_FORM_FILLED,
    COMPLETED, // everything is done
    DELETED; // soft deletion
}

fun toState(state: String): SignupState {
    try {
        return SignupState.valueOf(state.toUpperCase())
    } catch (ex: Exception) {
        throw IllegalArgumentException("invalid argument : status=$state")
    }
}