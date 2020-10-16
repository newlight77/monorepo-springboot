package io.tricefal.core.freelance

import io.tricefal.core.metafile.MetafileDomain
import java.time.Instant

data class FreelanceDomain
    constructor(
            val username: String,
            var resumeFile: MetafileDomain? = null
    ) {

    data class Builder(
            val username: String,
            var resumeFile: MetafileDomain? = null
    ) {
        fun resumeFile(resumeFile: MetafileDomain?) = apply { this.resumeFile = resumeFile }
        fun build() = FreelanceDomain(username, resumeFile)
    }
}

