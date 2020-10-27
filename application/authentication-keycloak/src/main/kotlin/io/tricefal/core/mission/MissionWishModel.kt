package io.tricefal.core.mission

import java.time.Instant

data class MissionWishModel
    constructor(
            val username: String,
            var summary: String?,
            var technologies: String?,
            var domains: String?,
            var clients: String?,
            var dailyFee: String?,
            var location: String?,
            var lastDate: Instant?

    ) {

    data class Builder(
            val username: String,
            var summary: String? = null,
            var technologies: String? = null,
            var domains: String? = null,
            var clients: String? = null,
            var dailyFee: String? = null,
            var location: String? = null,
            var lastDate: Instant? = null
    ) {
        fun summary(summary: String?) = apply { this.summary = summary }
        fun technologies(technologies: String?) = apply { this.technologies = technologies }
        fun domains(domains: String?) = apply { this.domains = domains }

        fun clients(clients: String?) = apply { this.clients = clients }
        fun dailyFee(dailyFee: String?) = apply { this.dailyFee = dailyFee }
        fun location(location: String?) = apply { this.location = location }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = MissionWishModel(
                username,
                summary,
                technologies,
                domains,
                clients,
                dailyFee,
                location,
                lastDate
        )
    }
}

fun toModel(domain: MissionWishDomain): MissionWishModel {
    return MissionWishModel.Builder(domain.username)
            .summary(domain.summary)
            .technologies(domain.technologies)
            .domains(domain.domains)
            .clients(domain.clients)
            .dailyFee(domain.dailyFee)
            .location(domain.location)
            .lastDate(domain.lastDate)
            .build()
}

fun fromModel(model: MissionWishModel): MissionWishDomain {
    return MissionWishDomain.Builder(model.username)
            .summary(model.summary)
            .technologies(model.technologies)
            .domains(model.domains)
            .clients(model.clients)
            .dailyFee(model.dailyFee)
            .location(model.location)
            .lastDate(model.lastDate)
            .build()
}
