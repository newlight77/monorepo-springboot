package io.oneprofile.core.cgu

import java.time.Instant

data class CguDomain(
        var username: String,
        var lastDate: Instant? = null,
        var acceptedCguVersion: String? = null) {

    data class Builder(
            val username: String,
            var lastDate: Instant? = null,
            var acceptedCguVersion: String? = null
    ) {
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }
        fun acceptedCguVersion(acceptedCguVersion: String?) = apply { this.acceptedCguVersion = acceptedCguVersion }

        fun build() = CguDomain(
                username =  username,
                lastDate = lastDate,
                acceptedCguVersion = acceptedCguVersion
        )
    }
}
