package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileModel
import java.time.Instant

data class SignupModel(val username: String,
                       val password: String,
                       val firstname: String,
                       val lastname: String,
                       val phoneNumber: String,
                       val signupDate: Instant = Instant.now(),

                       var activationCode: String? = null,
                       var status: Status? = null,
                       var metafile: MetafileModel? = null
                       )

//class SignupModel
//    private constructor(
//        val username: String,
//        val password: String,
//        val firstname: String,
//        val lastname: String,
//        val phoneNumber: String,
//        val signupDate: Instant = Instant.now(),
//
//        var activationCode: String?,
//        var status: Status?,
//        var metafile: MetafileModel? = null) {
//
//    data class Builder (
//            val username: String,
//            var password: String? = null,
//            var firstname: String? = null,
//            var lastname: String? = null,
//            var phoneNumber: String? = null,
//            var signupDate: Instant? = null,
//
//            var activationCode: String? = null,
//            var status: Status? = null,
//            var metafile: MetafileModel? = null
//    ) {
//        fun password(password: String) = apply { this.password = password }
//        fun firstname(firstname: String) = apply { this.firstname = firstname }
//        fun lastname(lastname: String) = apply { this.lastname = lastname }
//        fun phoneNumber(phoneNumber: String) = apply { this.phoneNumber = phoneNumber }
//        fun signupDate(signupDate: Instant) = apply { this.signupDate = signupDate }
//        fun activationCode(activationCode: String) = apply { this.activationCode = activationCode }
//        fun status(status: Status) = apply { this.status = status }
//        fun metafile(metafile: MetafileModel) = apply { this.metafile = metafile }
//    }
//}

fun toModel(domain: SignupDomain): SignupModel {
    val signup = SignupModel(
            domain.username,
            "",
            domain.firstname,
            domain.lastname,
            domain.phoneNumber,
            domain.signupDate,
            domain.activationcode,
            domain.status,
            domain.metafile?.let { io.tricefal.core.metafile.toModel(it) }
    )
    return signup
}

fun fromModel(model: SignupModel): SignupDomain {
    return SignupDomain(
            0,
            model.username,
            model.firstname,
            model.lastname,
            model.phoneNumber,
            model.signupDate,

            model.activationCode!!,
            model.status,
            model.metafile?.let { io.tricefal.core.metafile.fromModel(it) }
    )
}