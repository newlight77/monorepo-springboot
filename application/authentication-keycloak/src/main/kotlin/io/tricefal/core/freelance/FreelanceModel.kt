package io.tricefal.core.freelance

import io.tricefal.core.metafile.MetafileModel
import java.time.Instant


class FreelanceModel
    private constructor(
        val username: String,

        var resumeFile: MetafileModel? = null
    ) {

    data class Builder (
            val username: String,

            var resumeFile: MetafileModel? = null
    ) {

        fun resumeFile(resumeFile: MetafileModel?) = apply { this.resumeFile = resumeFile }

        fun build() = FreelanceModel(username,
                resumeFile
        )
    }
}

fun toModel(domain: FreelanceDomain): FreelanceModel {
    return FreelanceModel.Builder(domain.username)
            .resumeFile(domain.resumeFile?.let { io.tricefal.core.metafile.toModel(it) })
            .build()
}

fun fromModel(model: FreelanceModel): FreelanceDomain {
    return FreelanceDomain.Builder(model.username)
            .resumeFile(model.resumeFile?.let { io.tricefal.core.metafile.fromModel(it) })
            .build()
}