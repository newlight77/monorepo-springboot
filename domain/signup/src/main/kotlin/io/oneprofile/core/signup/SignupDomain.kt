package io.oneprofile.core.signup

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

            var resumeFilename: String? = null,
            var resumeLinkedinFilename: String? = null,

            var state: SignupStateDomain? = null,
            var comment: CommentDomain? = null,
            var lastDate: Instant? = null
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

            var resumeFilename: String? = null,
            var resumeLinkedinFilename: String? = null,

            var state: SignupStateDomain? = null,
            var comment: CommentDomain? = null,
            var lastDate: Instant? = null
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
        fun resumeFilename(resumeFilename: String?) = apply { this.resumeFilename = resumeFilename }
        fun resumeLinkedinFilename(resumeLinkedinFilename: String?) = apply { this.resumeLinkedinFilename = resumeLinkedinFilename }

        fun state(state: SignupStateDomain?) = apply {
            this.state = state ?: SignupStateDomain.Builder(username).build()
        }
        fun comment(comment: CommentDomain?) = apply { this.comment = comment }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

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
            resumeFilename = resumeFilename,
            resumeLinkedinFilename = resumeLinkedinFilename,
            state = state,
            comment = comment,
            lastDate = lastDate
        )
    }
}

enum class Status {
    NONE,
    CLIENT,
    FREELANCE,
    FREELANCE_WITH_MISSION,
    EMPLOYEE;
}

fun toStatus(status: String): Status {
    try {
        return Status.valueOf(status.toUpperCase())
    } catch (ex: Exception) {
        throw IllegalArgumentException("invalid argument : status=$status")
    }
}


enum class SignupState {
    NONE,
    SAVED,
    CGU_ACCEPTED,
    REGISTERED,
    SMS_CODE_SENT,
    SMS_CODE_VALIDATED,
    EMAIL_SENT,
    EMAIL_VALIDATED,
    RESUME_UPLOADED,
    RESUME_LINKEDIN_UPLOADED,
    STATUS_SET,
    VALIDATED, // activated by backoffice
    UNVALIDATED, // activated by backoffice
    MISSION_COMPLETED,
    COMPANY_COMPLETED,
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