package io.tricefal.core.freelance

import java.time.Instant

data class MotherCompanyModel(
    var raisonSocial: String? = null,
    var typeEntreprise: String? = null,
    var capital: String? = null,
    var creationDate: Instant? = null
)

fun toModel(domain: MotherCompanyDomain) : MotherCompanyModel {
    return MotherCompanyModel(
        domain.raisonSocial,
        domain.typeEntreprise,
        domain.capital,
        domain.creationDate
    )
}

fun fromModel(model: MotherCompanyModel) : MotherCompanyDomain {
    return MotherCompanyDomain(
        model.raisonSocial,
        model.typeEntreprise,
        model.capital,
        model.creationDate
    )
}