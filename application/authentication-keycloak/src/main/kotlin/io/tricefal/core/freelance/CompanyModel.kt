package io.tricefal.core.freelance


data class CompanyModel(
        val raisonSocial: String,
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

        var adminContact: ContactModel? = null,
        var bankInfo: BankInfoModel? = null,
        var fiscalAddress: AddressModel? = null

//        var kbisFile: MetafileModel? = null,
//        var ribFile: MetafileModel? = null,
//        var rcFile: MetafileModel? = null,
//        var urssafFile: MetafileModel? = null,
//        var fiscalFile: MetafileModel? = null
) {
    data class Builder(
            val raisonSocial: String,
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

            var adminContact: ContactModel? = null,
            var bankInfo: BankInfoModel? = null,
            var fiscalAddress: AddressModel? = null
    ) {
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

        fun adminContact(adminContact: ContactModel?) = apply { this.adminContact = adminContact }
        fun bankInfo(bankInfo: BankInfoModel?) = apply { this.bankInfo = bankInfo }
        fun fiscalAddress(fiscalAddress: AddressModel?) = apply { this.fiscalAddress = fiscalAddress }

        fun build() = CompanyModel(
                raisonSocial,
                nomCommercial,
                formeJuridique,
                capital,

                rcs,
                siret,
                numDuns,
                numTva,
                codeNaf,
                appartenanceGroupe,
                typeEntreprise,

                adminContact,
                bankInfo,
                fiscalAddress
        )
    }
}

fun toModel(domain: CompanyDomain) : CompanyModel {
    return CompanyModel(
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

            domain.adminContact?.let { toModel(it) },
            domain.bankInfo?.let { toModel(it) },
            domain.fiscalAddress?.let { toModel(it) }
    )
}

fun fromModel(model: CompanyModel) : CompanyDomain {
    return CompanyDomain(
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

            model.adminContact?.let { fromModel(it) },
            model.bankInfo?.let { fromModel(it) },
            model.fiscalAddress?.let { fromModel(it) }
    )
}