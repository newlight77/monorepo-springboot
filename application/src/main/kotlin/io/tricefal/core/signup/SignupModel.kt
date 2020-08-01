package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileModel
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

class SignupActivateModel( val username: String, val code: Int)
class SignupStatusModel( val username: String, val status: String)

class SignupModel
    private constructor(
        val username: String,
        val password: String?,
        val firstname: String?,
        val lastname: String?,
        val phoneNumber: String?,
        var status: Status?,
        val signupDate: Instant?,

        var resumeFile: MetafileModel? = null,
        var state: SignupStateModel? = null
    ) {

    data class Builder (
            val username: String,
            var password: String? = null,
            var firstname: String? = null,
            var lastname: String? = null,
            var phoneNumber: String? = null,
            var status: Status? = null,
            var signupDate: Instant? = null,

            var resumeFile: MetafileModel? = null,
            var state: SignupStateModel? = null
    ) {

        fun password(password: String?) = apply { this.password = password }
        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
        fun status(status: Status) = apply { this.status = status }
        fun signupDate(signupDate: Instant?) = apply { this.signupDate = signupDate }

        fun resumeFile(resumeFile: MetafileModel?) = apply { this.resumeFile = resumeFile }
        fun state(state: SignupStateModel?) = apply { this.state = state }

        fun build() = SignupModel(username,
                password,
                firstname,
                lastname,
                phoneNumber,
                status,
                signupDate ?: Instant.now(),
                resumeFile,
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
            .status(domain.status!!)
            .signupDate(domain.signupDate)
            .resumeFile(domain.resumeFile?.let { io.tricefal.core.metafile.toModel(it) })
            .state(domain.state?.let { toModel(it) })
            .build()
}

fun fromModel(model: SignupModel): SignupDomain {
    return SignupDomain.Builder(model.username)
            .password(model.password)
            .firstname(model.firstname)
            .lastname(model.lastname)
            .phoneNumber(model.phoneNumber)
            .status(model.status ?: Status.UNKNOWN)
            .signupDate(model.signupDate)
            .resumeFile(model.resumeFile?.let { io.tricefal.core.metafile.fromModel(it) })
            .state(model.state?.let { fromModel(it) })
            .build()
}