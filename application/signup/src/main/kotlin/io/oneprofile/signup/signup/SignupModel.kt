package io.oneprofile.signup.signup

import io.oneprofile.signup.metafile.originalFilename
import java.time.Instant

class SignupPasswordModel(val username: String, val newPassword: String)
class SignupResendModel(val username: String)
class SignupCodeModel(val code: String)
class SignupStatusModel(val status: String)

class SignupModel
    private constructor(
        val username: String,
        val password: String?,
        val firstname: String?,
        val lastname: String?,
        val phoneNumber: String?,
        var status: Status?,
        var cguAcceptedVersion: String? = null,
        val signupDate: Instant?,

        var resumeFilename: String? = null,
        var resumeLinkedinFilename: String? = null,

        var state: SignupStateModel? = null,

        var comment: CommentModel? = null,
        var lastDate: Instant? = null
    ) {

    data class Builder (
            val username: String,
            var password: String? = null,
            var firstname: String? = null,
            var lastname: String? = null,
            var phoneNumber: String? = null,
            var status: Status? = null,
            var cguAcceptedVersion: String? = null,
            var signupDate: Instant? = null,

            var resumeFilename: String? = null,
            var resumeLinkedinFilename: String? = null,

            var state: SignupStateModel? = null,

            var comment: CommentModel? = null,
            var lastDate: Instant? = null
    ) {

        fun password(password: String?) = apply { this.password = password }
        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
        fun status(status: Status) = apply { this.status = status }
        fun cguAcceptedVersion(cguAcceptedVersion: String?) = apply { this.cguAcceptedVersion = cguAcceptedVersion }
        fun signupDate(signupDate: Instant?) = apply { this.signupDate = signupDate }

        fun resumeFilename(resumeFilename: String?) = apply { this.resumeFilename = resumeFilename }
        fun resumeLinkedinFilename(resumeLinkedinFilename: String?) = apply { this.resumeLinkedinFilename = resumeLinkedinFilename }
        fun state(state: SignupStateModel?) = apply {
            this.state = state ?: SignupStateModel.Builder(username).build()
        }
        fun comment(comment: CommentModel?) = apply { this.comment = comment }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = SignupModel(
            username = username,
            password = password,
            firstname = firstname,
            lastname = lastname,
            phoneNumber = phoneNumber,
            status = status,
            signupDate = signupDate,
            cguAcceptedVersion = cguAcceptedVersion,
            resumeFilename = resumeFilename,
            resumeLinkedinFilename = resumeLinkedinFilename,
            state = state,
            comment = comment,
            lastDate = lastDate
        )
    }
}

fun toModel(domain: SignupDomain): SignupModel {
    return SignupModel.Builder(domain.username)
        .password(domain.password)
        .firstname(domain.firstname)
        .lastname(domain.lastname)
        .phoneNumber(domain.phoneNumber)
        .status(domain.status!!)
        .signupDate(domain.signupDate)
        .resumeFilename(originalFilename(domain.resumeFilename))
        .resumeLinkedinFilename(originalFilename(domain.resumeLinkedinFilename))
        .cguAcceptedVersion(domain.cguAcceptedVersion)
        .state(domain.state?.let { toModel(it) })
        .lastDate(domain.lastDate)
        .comment(domain.comment?.let { toModel(it) })
        .build()
}

fun fromModel(model: SignupModel): SignupDomain {
    return SignupDomain.Builder(model.username)
        .password(model.password)
        .firstname(model.firstname)
        .lastname(model.lastname)
        .phoneNumber(model.phoneNumber)
        .status(model.status ?: Status.NONE)
        .signupDate(model.signupDate ?: Instant.now())
        .resumeFilename(model.resumeFilename)
        .resumeLinkedinFilename(model.resumeLinkedinFilename)
        .cguAcceptedVersion(model.cguAcceptedVersion)
        .state(model.state?.let { fromModel(it) })
        .comment(model.comment?.let { fromModel(it) })
        .lastDate(model.lastDate)
        .build()
}