package io.oneprofile.core.profile

import io.oneprofile.core.metafile.originalFilename
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

        var portraitFilename: String? = null,
        var resumeFilename: String? = null,
        var resumeLinkedinFilename: String? = null,

    ) {

    data class Builder (
        val username: String,
        var firstname: String? = null,
        var lastname: String? = null,
        var phoneNumber: String? = null,

        var status: Status? = null,
        var state: ProfileStateModel? = null,
        var lastDate: Instant? = null,

        var portraitFilename: String? = null,
        var resumeFilename: String? = null,
        var resumeLinkedinFilename: String? = null,
    ) {

        fun firstname(firstname: String?) = apply { this.firstname = firstname }
        fun lastname(lastname: String?) = apply { this.lastname = lastname }
        fun phoneNumber(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }

        fun status(status: Status?) = apply { this.status = status }
        fun state(state: ProfileStateModel?) = apply { this.state = state }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun portraitFilename(portraitFilename: String?) = apply { this.portraitFilename = portraitFilename }
        fun resumeFilename(resumeFilename: String?) = apply { this.resumeFilename = resumeFilename }
        fun resumeLinkedinFilename(resumeLinkedinFilename: String?) = apply { this.resumeLinkedinFilename = resumeLinkedinFilename }

        fun build() = ProfileModel(
            username = username,
            firstname = firstname,
            lastname = lastname,
            phoneNumber = phoneNumber,

            status = status,
            state = state,
            lastDate = lastDate ?: Instant.now(),

            portraitFilename = portraitFilename,
            resumeFilename = resumeFilename,
            resumeLinkedinFilename = resumeLinkedinFilename
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
        .portraitFilename(originalFilename(domain.portraitFilename))
        .resumeFilename(originalFilename(domain.resumeFilename))
        .resumeLinkedinFilename(originalFilename(domain.resumeLinkedinFilename))
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
        .portraitFilename(model.portraitFilename)
        .resumeFilename(model.resumeFilename)
        .resumeLinkedinFilename(model.resumeLinkedinFilename)
        .build()
}