package io.tricefal.core.freelance

import java.time.Instant
import java.time.LocalDate

class PrivacyDetailDomain(
        val username: String,
        val birthDate: LocalDate?,
        val birthCity: String?,
        val birthCountry: String?,
        val citizenship: String?,
        val socialSecurityNumber: String?,
        val nationalIdentityNumber: String?,
        val information: String?,
        val lastDate: Instant?
) {
    data class Builder(
            val username: String,
            var birthDate: LocalDate? = null,
            var birthCity: String? = null,
            var birthCountry: String? = null,
            var citizenship: String? = null,
            var socialSecurityNumber: String? = null,
            var nationalIdentityNumber: String? = null,
            var information: String? = null,
            var lastDate: Instant? = null
    ) {
        fun birthDate(birthDate: LocalDate?) = apply { this.birthDate = birthDate }
        fun birthCity(birthCity: String?) = apply { this.birthCity = birthCity }
        fun birthCountry(birthCountry: String?) = apply { this.birthCountry = birthCountry }
        fun citizenship(citizenship: String?) = apply { this.citizenship = citizenship }
        fun socialSecurityNumber(socialSecurityNumber: String?) = apply { this.socialSecurityNumber = socialSecurityNumber }
        fun nationalIdentityNumber(nationalIdentityNumber: String?) = apply { this.nationalIdentityNumber = nationalIdentityNumber }
        fun information(information: String?) = apply { this.information = information }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = PrivacyDetailDomain(
                username = username,
                birthDate = birthDate,
                birthCity = birthCity,
                birthCountry = birthCountry,
                citizenship = citizenship,
                socialSecurityNumber = socialSecurityNumber,
                nationalIdentityNumber = nationalIdentityNumber,
                information = information,
                lastDate = lastDate
        )
    }

}