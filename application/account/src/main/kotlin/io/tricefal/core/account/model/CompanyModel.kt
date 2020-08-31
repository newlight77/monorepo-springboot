package io.tricefal.core.account.model

import io.tricefal.core.account.domain.CompanyDomain

data class CompanyModel(
        val id: Long,
        val raisonSocial: String,
        val nomCommercial: String,
        val formeJuridique: String,
        val capital: String,
        val rcs: String,
        val siret: String,
        val numDuns: String,
        val numTva: String,
        val codeNaf: String,
        val appartenanceGroupe: String,
        val typeEntreprise: String,
        val kbisUrl: String,
        val ribUrl: String,
        val rcUrl: String,
        val contact: ContactModel,
        val bankInfo: BankInfoModel
)

fun fromDomain(domain: CompanyDomain) : CompanyModel {
    return CompanyModel(
            domain.id,
            domain.raisonSocial,
            domain.nomCommercial,
            domain.formeJuridique,
            domain.capital,
            domain.rcs,
            domain.siret,
            domain.numDuns,
            domain.numTva,
            domain.codeNaf,
            domain.appartenanceGroupe,
            domain.typeEntreprise,
            domain.kbisUrl,
            domain.ribUrl,
            domain.rcUrl,
            fromDomain(domain.contact),
            fromDomain(domain.bankInfo)
    )
}

fun toDomain(model: CompanyModel) : CompanyDomain {
    return CompanyDomain(
            model.id,
            model.raisonSocial,
            model.nomCommercial,
            model.formeJuridique,
            model.capital,
            model.rcs,
            model.siret,
            model.numDuns,
            model.numTva,
            model.codeNaf,
            model.appartenanceGroupe,
            model.typeEntreprise,
            model.kbisUrl,
            model.ribUrl,
            model.rcUrl,
            toDomain(model.contact),
            toDomain(model.bankInfo)
    )
}