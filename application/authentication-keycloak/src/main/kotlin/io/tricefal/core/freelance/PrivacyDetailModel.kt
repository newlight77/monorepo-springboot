package io.tricefal.core.freelance

import java.time.LocalDate

data class PrivacyDetailModel(
        val username: String,
        val birthDate: LocalDate?,
        val birthCity: String?,
        val birthCountry: String?,
        val citizenship: String?,
        val socialSecurityNumber: String?,
        val nationalIdentityNumber: String?,
        val information: String?
) {
    data class Builder(
            val username: String,
            var birthDate: LocalDate? = null,
            var birthCity: String? = null,
            var birthCountry: String? = null,
            var citizenship: String? = null,
            var socialSecurityNumber: String? = null,
            var nationalIdentityNumber: String? = null,
            var information: String? = null
    ) {
        fun birthDate(birthDate: LocalDate?) = apply { this.birthDate = birthDate }
        fun birthCity(birthCity: String?) = apply { this.birthCity = birthCity }
        fun birthCountry(birthCountry: String?) = apply { this.birthCountry = birthCountry }
        fun citizenship(citizenship: String?) = apply { this.citizenship = citizenship }
        fun socialSecurityNumber(socialSecurityNumber: String?) = apply { this.socialSecurityNumber = socialSecurityNumber }
        fun nationalIdentityNumber(nationalIdentityNumber: String?) = apply { this.nationalIdentityNumber = nationalIdentityNumber }
        fun information(information: String?) = apply { this.information = information }

        fun build() = PrivacyDetailModel(
                username = username,
                birthDate = birthDate,
                birthCity = birthCity,
                birthCountry = birthCountry,
                citizenship = citizenship,
                socialSecurityNumber = socialSecurityNumber,
                nationalIdentityNumber = nationalIdentityNumber,
                information = information
        )
    }
}

fun toModel(domain: PrivacyDetailDomain) : PrivacyDetailModel {
    return PrivacyDetailModel(
            username = domain.username,
            birthDate = domain.birthDate,
            birthCity = domain.birthCity,
            birthCountry = domain.birthCountry,
            citizenship = domain.citizenship,
            socialSecurityNumber = domain.socialSecurityNumber,
            nationalIdentityNumber = domain.nationalIdentityNumber,
            information = domain.information
    )
}

fun fromModel(model: PrivacyDetailModel): PrivacyDetailDomain {
    return PrivacyDetailDomain(
            username = model.username,
            birthDate = model.birthDate,
            birthCity = model.birthCity,
            birthCountry = model.birthCountry,
            citizenship = model.citizenship,
            socialSecurityNumber = model.socialSecurityNumber,
            nationalIdentityNumber = model.nationalIdentityNumber,
            information = model.information
    )
}