package io.tricefal.core.account.domain

class CompanyDomain(
        var id: Long,
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
        val contact: ContactDomain,
        val bankInfo: BankInfoDomain
) {
}