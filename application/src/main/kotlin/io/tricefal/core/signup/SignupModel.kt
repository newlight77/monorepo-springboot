package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileModel
import java.time.Instant

class SignupModel
    private constructor(
        val username: String,
        val password: String?,
        val firstname: String?,
        val lastname: String?,
        val phoneNumber: String?,

        val signupDate: Instant?,

        var activationCode: String?,
        var status: Status?,
        var metafile: MetafileModel? = null) {

    data class Builder (
            val username: String,
            var password: String? = null,
            var firstname: String? = null,
            var lastname: String? = null,
            var phoneNumber: String? = null,
            var signupDate: Instant? = null,

            var activationCode: String? = null,
            var status: Status? = null,
            var metafile: MetafileModel? = null
    ) {
        fun password(password: String?) = apply { this.password = password }
        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }

        fun signupDate(signupDate: Instant?) = apply { this.signupDate = signupDate }

        fun activationCode(activationCode: String?) = apply { this.activationCode = activationCode }
        fun status(status: Status) = apply { this.status = status }
        fun metafile(metafile: MetafileModel?) = apply { this.metafile = metafile }

        fun build() = SignupModel(username, password, firstname, lastname,
                phoneNumber, signupDate ?: Instant.now(), activationCode, status, metafile)
    }
}

fun toModel(domain: SignupDomain): SignupModel {
    return SignupModel.Builder(domain.username)
            .password(domain.password)
            .firstname(domain.firstname)
            .lastname(domain.lastname)
            .phoneNumber(domain.phoneNumber)
            .signupDate(domain.signupDate)
            .activationCode(domain.activationCode)
            .status(domain.status!!)
            .metafile(domain.metafile?.let { io.tricefal.core.metafile.toModel(it) })
            .build()
}

fun fromModel(model: SignupModel): SignupDomain {
    return SignupDomain.Builder(model.username)
            .password(model.password)
            .firstname(model.firstname)
            .lastname(model.lastname)
            .phoneNumber(model.phoneNumber)
            .signupDate(model.signupDate)
            .activationCode(model.activationCode)
            .status(model.status ?: Status.UNKNOWN)
            .metafile(model.metafile?.let { io.tricefal.core.metafile.fromModel(it) })
            .build()
}