package io.oneprofile.signup.mission

import java.time.Instant

data class MissionWishDomain
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

        fun build() = MissionWishDomain(
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

