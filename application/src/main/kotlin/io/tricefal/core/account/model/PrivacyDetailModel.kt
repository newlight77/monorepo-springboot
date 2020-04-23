package io.tricefal.core.account.model

import io.tricefal.core.account.domain.PrivacyDetailDomain
import java.time.LocalDate

data class PrivacyDetailModel(
        var id: Long,
        val birthDate: LocalDate,
        val birthCity: String,
        val birthCountry: String,
        val citizenship: String,
        val socialSecurityNumber: String,
        val information: String,
        val cinUrl: String,
        val socialSecurityUrl: String,
        val visaUrl: String,
        val diplomaUrl: String,
        val casierJudiciaireUrl: String
)

fun fromDomain(domain: PrivacyDetailDomain) : PrivacyDetailModel {
    return PrivacyDetailModel(
            domain.id,
            domain.birthDate,
            domain.birthCity,
            domain.birthCountry,
            domain.citizenship,
            domain.socialSecurityNumber,
            domain.information,
            domain.cinUrl,
            domain.socialSecurityUrl,
            domain.visaUrl,
            domain.diplomaUrl,
            domain.casierJudiciaireUrl
    )
}

fun toDomain(model: PrivacyDetailModel): PrivacyDetailDomain {
    return PrivacyDetailDomain(
            model.id,
            model.birthDate,
            model.birthCity,
            model.birthCountry,
            model.citizenship,
            model.socialSecurityNumber,
            model.information,
            model.cinUrl,
            model.socialSecurityUrl,
            model.visaUrl,
            model.diplomaUrl,
            model.casierJudiciaireUrl
    )
}