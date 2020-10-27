package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileModel
import io.tricefal.core.metafile.Representation
import java.time.Instant


class ProfileModel
    private constructor(
        val username: String,
        val lastDate: Instant?,

        var portraitFile: MetafileModel? = null,
        var resumeFile: MetafileModel? = null,
        var resumeLinkedinFile: MetafileModel? = null
    ) {

    data class Builder (
            val username: String,
            var lastDate: Instant? = null,

            var portraitFile: MetafileModel? = null,
            var resumeFile: MetafileModel? = null,
            var resumeLinkedinFile: MetafileModel? = null
    ) {

        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun portraitFile(portraitFile: MetafileModel?) = apply { this.portraitFile = portraitFile }
        fun resumeFile(resumeFile: MetafileModel?) = apply { this.resumeFile = resumeFile }
        fun resumeLinkedinFile(resumeLinkedinFile: MetafileModel?) = apply { this.resumeLinkedinFile = resumeLinkedinFile }

        fun build() = ProfileModel(username,
                lastDate ?: Instant.now(),
                portraitFile,
                resumeFile,
                resumeLinkedinFile
        )
    }
}

fun toModel(domain: ProfileDomain): ProfileModel {
    return ProfileModel.Builder(domain.username)
            .lastDate(domain.lastDate)
            .portraitFile(domain.portraitFilename?.let { MetafileModel.Builder(domain.username, it, Representation.PORTRAIT).build() })
            .resumeFile(domain.resumeFilename?.let { MetafileModel.Builder(domain.username, it, Representation.CV).build() })
            .resumeLinkedinFile(domain.resumeLinkedinFilename?.let { MetafileModel.Builder(domain.username, it, Representation.CV_LINKEDIN).build() })
            .build()
}

fun fromModel(model: ProfileModel): ProfileDomain {
    return ProfileDomain.Builder(model.username)
            .lastDate(model.lastDate)
            .portraitFilename(model.portraitFile?.filename)
            .resumeFilename(model.resumeFile?.filename)
            .resumeLinkedinFilename(model.resumeLinkedinFile?.filename)
            .build()
}