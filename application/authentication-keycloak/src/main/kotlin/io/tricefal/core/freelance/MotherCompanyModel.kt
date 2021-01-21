package io.tricefal.core.freelance

data class MotherCompanyModel(
    var raisonSocial: String? = null,
    var typeEntreprise: String? = null
)

fun toModel(domain: MotherCompanyDomain) : MotherCompanyModel {
    return MotherCompanyModel(
        domain.raisonSocial,
        domain.typeEntreprise
    )
}

fun fromModel(model: MotherCompanyModel) : MotherCompanyDomain {
    return MotherCompanyDomain(
        model.raisonSocial,
        model.typeEntreprise
    )
}