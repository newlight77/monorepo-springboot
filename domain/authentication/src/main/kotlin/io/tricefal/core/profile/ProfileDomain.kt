package io.tricefal.core.profile

import java.time.Instant

data class ProfileDomain(
        var username: String,
        var lastDate: Instant? = Instant.now(),
        var portraitFilename: String? = null,
        var resumeFilename: String? = null,
        var refFilename: String? = null
    ) {

    data class Builder(
            val username: String,
            var lastDate: Instant? = null,

            var portraitFilename: String? = null,
            var resumeFilename: String? = null,
            var refFilename: String? = null
    ) {
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }
        fun portraitFilename(portraitFilename: String?) = apply { this.portraitFilename = portraitFilename }
        fun resumeFilename(resumeFilename: String?) = apply { this.resumeFilename = resumeFilename }
        fun refFilename(refFilename: String?) = apply { this.refFilename = refFilename }

        fun build() = ProfileDomain(username,
                lastDate,
                portraitFilename,
                resumeFilename,
                refFilename
        )
    }
}
