package io.tricefal.core.mission

import org.springframework.boot.availability.ApplicationAvailability
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
            var missionAbroad: String?,
            var resumeFilename: String?,
            var availabilityDate: Instant?,
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
            var missionAbroad: String? = null,
            var resumeFilename: String? = null,
            var availabilityDate: Instant? = null,
            var lastDate: Instant? = null
    ) {
        fun summary(summary: String?) = apply { this.summary = summary }
        fun technologies(technologies: String?) = apply { this.technologies = technologies }
        fun domains(domains: String?) = apply { this.domains = domains }

        fun clients(clients: String?) = apply { this.clients = clients }
        fun dailyFee(dailyFee: String?) = apply { this.dailyFee = dailyFee }
        fun location(location: String?) = apply { this.location = location }
        fun missionAbroad(missionAbroad: String?) = apply { this.missionAbroad = missionAbroad }
        fun resumeFilename(resumeFilename: String?) = apply { this.resumeFilename = resumeFilename }
        fun availabilityDate(availabilityDate: Instant?) = apply { this.availabilityDate = availabilityDate }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = MissionWishModel(
            username = username,
            summary = summary,
            technologies = technologies,
            domains = domains,
            clients = clients,
            dailyFee = dailyFee,
            location = location,
            missionAbroad = missionAbroad,
            resumeFilename = resumeFilename,
            availabilityDate = availabilityDate,
            lastDate = lastDate
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
            .missionAbroad(domain.missionAbroad)
            .resumeFilename(domain.resumeFilename)
            .availabilityDate(domain.availabilityDate)
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
            .missionAbroad(model.missionAbroad)
            .resumeFilename(model.resumeFilename)
            .availabilityDate(model.availabilityDate)
            .lastDate(model.lastDate)
            .build()
}
