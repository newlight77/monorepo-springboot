package io.tricefal.core.freelance

class CompanyDomain(
        val raisonSocial: String?,
        val nomCommercial: String?,
        val formeJuridique: String?,
        val capital: String?,
        val rcs: String?,
        val siret: String?,
        val numDuns: String?,
        val numTva: String?,
        val codeNaf: String?,
        val appartenanceGroupe: String?,
        val typeEntreprise: String?,

        val adminContact: ContactDomain?,
        val bankInfo: BankInfoDomain?,
        val fiscalAddress: AddressDomain?
) {
    data class Builder(
            var raisonSocial: String? = null,
            var nomCommercial: String? = null,
            var formeJuridique: String? = null,
            var capital: String? = null,
            var rcs: String? = null,
            var siret: String? = null,
            var numDuns: String? = null,
            var numTva: String? = null,
            var codeNaf: String? = null,
            var appartenanceGroupe: String? = null,
            var typeEntreprise: String? = null,

            var adminContact: ContactDomain? = null,
            var bankInfo: BankInfoDomain? = null,
            var fiscalAddress: AddressDomain? = null
    ) {
        fun raisonSocial(raisonSocial: String?) = apply { this.raisonSocial = raisonSocial }
        fun nomCommercial(nomCommercial: String?) = apply { this.nomCommercial = nomCommercial }
        fun formeJuridique(formeJuridique: String?) = apply { this.formeJuridique = formeJuridique }
        fun capital(capital: String?) = apply { this.capital = capital }
        fun rcs(rcs: String?) = apply { this.rcs = rcs }
        fun siret(siret: String?) = apply { this.siret = siret }
        fun numDuns(numDuns: String?) = apply { this.numDuns = numDuns }
        fun numTva(numTva: String?) = apply { this.numTva = numTva }
        fun codeNaf(codeNaf: String?) = apply { this.codeNaf = codeNaf }
        fun appartenanceGroupe(appartenanceGroupe: String?) = apply { this.appartenanceGroupe = appartenanceGroupe }
        fun typeEntreprise(typeEntreprise: String?) = apply { this.typeEntreprise = typeEntreprise }
        fun adminContact(adminContact: ContactDomain?) = apply { this.adminContact = adminContact }
        fun bankInfo(bankInfo: BankInfoDomain?) = apply { this.bankInfo = bankInfo }
        fun fiscalAddress(fiscalAddress: AddressDomain?) = apply { this.fiscalAddress = fiscalAddress }

        fun build() = CompanyDomain(
                raisonSocial = raisonSocial,
                nomCommercial = nomCommercial,
                formeJuridique = formeJuridique,
                capital = capital,

                rcs = rcs,
                siret = siret,
                numDuns = numDuns,
                numTva = numTva,
                codeNaf = codeNaf,
                appartenanceGroupe = appartenanceGroupe,
                typeEntreprise = typeEntreprise,

                adminContact = adminContact,
                bankInfo = bankInfo,
                fiscalAddress = fiscalAddress
        )
    }
}