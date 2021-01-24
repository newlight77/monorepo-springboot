package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileModel
import java.time.Instant

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
        val signupDate: Instant?,

        var cguAcceptedVersion: String? = null,
        var resumeFile: MetafileModel? = null,
        var state: SignupStateModel? = null,

        var lastDate: Instant? = null
    ) {

    data class Builder (
            val username: String,
            var password: String? = null,
            var firstname: String? = null,
            var lastname: String? = null,
            var phoneNumber: String? = null,
            var status: Status? = null,
            var signupDate: Instant? = null,

            var cguAcceptedVersion: String? = null,
            var resumeFile: MetafileModel? = null, // not used
            var state: SignupStateModel? = null,

            var lastDate: Instant? = null
    ) {

        fun password(password: String?) = apply { this.password = password }
        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
        fun status(status: Status) = apply { this.status = status }
        fun signupDate(signupDate: Instant?) = apply { this.signupDate = signupDate }

        fun cguAcceptedVersion(cguAcceptedVersion: String?) = apply { this.cguAcceptedVersion = cguAcceptedVersion }
        fun resumeFile(resumeFile: MetafileModel?) = apply { this.resumeFile = resumeFile }
        fun state(state: SignupStateModel?) = apply {
            this.state = state ?: SignupStateModel.Builder(username).build()
        }
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
                resumeFile = resumeFile,
                state = state,
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
            .resumeFile(domain.resumeFile?.let { io.tricefal.core.metafile.toModel(it) })
            .cguAcceptedVersion(domain.cguAcceptedVersion)
            .state(domain.state?.let { toModel(it) })
            .lastDate(domain.lastDate)
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
            .resumeFile(model.resumeFile?.let { io.tricefal.core.metafile.fromModel(it) })
            .cguAcceptedVersion(model.cguAcceptedVersion)
            .state(model.state?.let { fromModel(it) })
            .lastDate(model.lastDate)
            .build()
}