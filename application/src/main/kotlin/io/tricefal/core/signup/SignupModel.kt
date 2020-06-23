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
        var activationCode: String?,
        var status: Status?,
        val signupDate: Instant?,

        var resumeFile: MetafileModel? = null,
//        var notification: SignupNotificationModel? = null,
        var state: SignupStateModel? = null
    ) {

    data class Builder (
            val username: String,
            var password: String? = null,
            var firstname: String? = null,
            var lastname: String? = null,
            var phoneNumber: String? = null,
            var activationCode: String? = null,
            var status: Status? = null,
            var signupDate: Instant? = null,

            var resumeFile: MetafileModel? = null,
//            var notification: SignupNotificationModel? = null,
            var state: SignupStateModel? = null
    ) {

        fun password(password: String?) = apply { this.password = password }
        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
        fun activationCode(activationCode: String?) = apply { this.activationCode = activationCode }
        fun status(status: Status) = apply { this.status = status }
        fun signupDate(signupDate: Instant?) = apply { this.signupDate = signupDate }

        fun resumeFile(resumeFile: MetafileModel?) = apply { this.resumeFile = resumeFile }
//        fun notification(notification: SignupNotificationModel?) = apply { this.notification = notification }
        fun state(state: SignupStateModel?) = apply { this.state = state }

        fun build() = SignupModel(username,
                password,
                firstname,
                lastname,
                phoneNumber,
                activationCode,
                status,
                signupDate ?: Instant.now(),
                resumeFile,
//                notification,
                state
        )
    }
}

fun toModel(domain: SignupDomain): SignupModel {
    return SignupModel.Builder(domain.username)
            .password(domain.password)
            .firstname(domain.firstname)
            .lastname(domain.lastname)
            .phoneNumber(domain.phoneNumber)
            .activationCode(domain.activationCode)
            .status(domain.status!!)
            .signupDate(domain.signupDate)
            .resumeFile(domain.resumeFile?.let { io.tricefal.core.metafile.toModel(it) })
//            .notification(toModel(domain.notification!!))
            .state(toModel(domain.state!!))
            .build()
}

fun fromModel(model: SignupModel): SignupDomain {
    return SignupDomain.Builder(model.username)
            .password(model.password)
            .firstname(model.firstname)
            .lastname(model.lastname)
            .phoneNumber(model.phoneNumber)
            .activationCode(model.activationCode)
            .status(model.status ?: Status.UNKNOWN)
            .signupDate(model.signupDate)
            .resumeFile(model.resumeFile?.let { io.tricefal.core.metafile.fromModel(it) })
            .state(fromModel(model.state!!))
            .build()
}