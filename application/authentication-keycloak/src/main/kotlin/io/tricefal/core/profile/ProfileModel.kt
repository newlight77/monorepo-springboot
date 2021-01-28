package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileModel
import io.tricefal.core.metafile.Representation
import java.time.Instant


class ProfileModel
    private constructor(
        val username: String,
        var firstname: String?,
        var lastname: String?,
        var phoneNumber: String?,

        var status: Status?,
        var state: ProfileStateModel? = null,
        val lastDate: Instant?,

        var portraitFile: MetafileModel? = null,
        var resumeFile: MetafileModel? = null,
        var resumeLinkedinFile: MetafileModel? = null
    ) {

    data class Builder (
        val username: String,
        var firstname: String? = null,
        var lastname: String? = null,
        var phoneNumber: String? = null,

        var status: Status? = null,
        var state: ProfileStateModel? = null,
        var lastDate: Instant? = null,

        var portraitFile: MetafileModel? = null,
        var resumeFile: MetafileModel? = null,
        var resumeLinkedinFile: MetafileModel? = null
    ) {

        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }

        fun status(status: Status?) = apply { this.status = status }
        fun state(state: ProfileStateModel?) = apply { this.state = state }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun portraitFile(portraitFile: MetafileModel?) = apply { this.portraitFile = portraitFile }
        fun resumeFile(resumeFile: MetafileModel?) = apply { this.resumeFile = resumeFile }
        fun resumeLinkedinFile(resumeLinkedinFile: MetafileModel?) = apply { this.resumeLinkedinFile = resumeLinkedinFile }

        fun build() = ProfileModel(
            username = username,
            firstname = firstname,
            lastname = lastname,
            phoneNumber = phoneNumber,

            status = status,
            state = state,
            lastDate = lastDate ?: Instant.now(),

            portraitFile = portraitFile,
            resumeFile = resumeFile,
            resumeLinkedinFile = resumeLinkedinFile
        )
    }
}

fun toModel(domain: ProfileDomain): ProfileModel {
    return ProfileModel.Builder(domain.username)
        .firstname(domain.firstname)
        .lastname(domain.lastname)
        .phoneNumber(domain.phoneNumber)
        .status(domain.status)
        .state(domain.state?.let { toModel(it) })
        .lastDate(domain.lastDate)
        .portraitFile(domain.portraitFilename?.let { MetafileModel.Builder(domain.username, it, Representation.PORTRAIT).build() })
        .resumeFile(domain.resumeFilename?.let { MetafileModel.Builder(domain.username, it, Representation.CV).build() })
        .resumeLinkedinFile(domain.resumeLinkedinFilename?.let { MetafileModel.Builder(domain.username, it, Representation.CV_LINKEDIN).build() })
        .build()
}

fun fromModel(model: ProfileModel): ProfileDomain {
    return ProfileDomain.Builder(model.username)
        .firstname(model.firstname)
        .lastname(model.lastname)
        .phoneNumber(model.phoneNumber)
        .status(model.status)
        .state(model.state?.let { fromModel(it) })
        .lastDate(model.lastDate)
        .portraitFilename(model.portraitFile?.filename)
        .resumeFilename(model.resumeFile?.filename)
        .resumeLinkedinFilename(model.resumeLinkedinFile?.filename)
        .build()
}