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
                username,
                birthDate!!,
                birthCity!!,
                birthCountry!!,
                citizenship!!,
                socialSecurityNumber!!,
                nationalIdentityNumber!!,
                information!!
        )
    }
}

fun toModel(domain: PrivacyDetailDomain) : PrivacyDetailModel {
    return PrivacyDetailModel(
            domain.username,
            domain.birthDate,
            domain.birthCity,
            domain.birthCountry,
            domain.citizenship,
            domain.socialSecurityNumber,
            domain.nationalIdentityNumber,
            domain.information
    )
}

fun fromModel(model: PrivacyDetailModel): PrivacyDetailDomain {
    return PrivacyDetailDomain(
            model.username,
            model.birthDate,
            model.birthCity,
            model.birthCountry,
            model.citizenship,
            model.socialSecurityNumber,
            model.nationalIdentityNumber,
            model.information
    )
}