package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileModel
import java.time.Instant


class ProfileModel
    private constructor(
        val username: String,
        val lastDate: Instant?,

        var portraitFile: MetafileModel? = null,
        var resumeFile: MetafileModel? = null,
        var refFile: MetafileModel? = null
    ) {

    data class Builder (
            val username: String,
            var lastDate: Instant? = null,

            var portraitFile: MetafileModel? = null,
            var resumeFile: MetafileModel? = null,
            var refFile: MetafileModel? = null
    ) {

        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun portraitFile(portraitFile: MetafileModel?) = apply { this.portraitFile = portraitFile }
        fun resumeFile(resumeFile: MetafileModel?) = apply { this.resumeFile = resumeFile }
        fun refFile(refFile: MetafileModel?) = apply { this.refFile = refFile }

        fun build() = ProfileModel(username,
                lastDate ?: Instant.now(),
                portraitFile,
                resumeFile,
                refFile
        )
    }
}

fun toModel(domain: ProfileDomain): ProfileModel {
    return ProfileModel.Builder(domain.username)
            .lastDate(domain.lastDate)
            .portraitFile(domain.resumeFilename?.let { MetafileModel.Builder().filename(it).build() })
            .resumeFile(domain.resumeFilename?.let { MetafileModel.Builder().filename(it).build() })
            .refFile(domain.resumeFilename?.let { MetafileModel.Builder().filename(it).build() })
            .build()
}

fun fromModel(model: ProfileModel): ProfileDomain {
    return ProfileDomain.Builder(model.username)
            .lastDate(model.lastDate)
            .portraitFilename(model.portraitFile?.filename)
            .resumeFilename(model.resumeFile?.filename)
            .refFilename(model.refFile?.filename)
            .build()
}