package io.tricefal.core.cgu

import java.time.Instant


class CguModel
    private constructor(
        val username: String,
        var lastDate: Instant?,
        var cguVersion: String? = null
        
    ) {

    data class Builder (
            val username: String,
            var lastDate: Instant? = null,
            var cguVersion: String? = null
    ) {

        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }
        fun cguVersion(cguVersion: String?) = apply { this.cguVersion = cguVersion }

        fun build() = CguModel(username,
                lastDate ?: Instant.now(),
                cguVersion

        )
    }
}

fun toModel(domain: CguDomain): CguModel {
    return CguModel.Builder(domain.username)
            .lastDate(domain.lastDate)
            .cguVersion(domain.acceptedCguVersion)
            .build()
}

fun fromModel(model: CguModel): CguDomain {
    return CguDomain.Builder(model.username)
            .lastDate(model.lastDate)
            .acceptedCguVersion(model.cguVersion)
            .build()
}