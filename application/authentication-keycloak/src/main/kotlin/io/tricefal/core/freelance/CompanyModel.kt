package io.tricefal.core.freelance

import java.time.Instant


data class CompanyModel(
        val companyName: String,
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

        var adminContact: ContactModel? = null,
        var bankInfo: BankInfoModel? = null,
        var fiscalAddress: AddressModel? = null,

        var state: CompanyStateModel? = null,
        var lastDate: Instant? = null

//        var kbisFile: MetafileModel? = null,
//        var ribFile: MetafileModel? = null,
//        var rcFile: MetafileModel? = null,
//        var urssafFile: MetafileModel? = null,
//        var fiscalFile: MetafileModel? = null
) {
    data class Builder(
            var companyName: String,
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

            var adminContact: ContactModel? = null,
            var bankInfo: BankInfoModel? = null,
            var fiscalAddress: AddressModel? = null,

            var state: CompanyStateModel? = null,
            var lastDate: Instant? = null
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

        fun adminContact(adminContact: ContactModel?) = apply { this.adminContact = adminContact }
        fun bankInfo(bankInfo: BankInfoModel?) = apply { this.bankInfo = bankInfo }
        fun fiscalAddress(fiscalAddress: AddressModel?) = apply { this.fiscalAddress = fiscalAddress }
        fun state(state: CompanyStateModel?) = apply { this.state = state }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = CompanyModel(
            companyName = companyName,
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
            fiscalAddress = fiscalAddress,
            state = state,
            lastDate = lastDate
        )
    }
}

fun toModel(domain: CompanyDomain) : CompanyModel {
    return CompanyModel.Builder(domain.companyName)
        .raisonSocial(domain.raisonSocial)
        .nomCommercial(domain.nomCommercial)
        .formeJuridique(domain.formeJuridique)
        .capital(domain.capital)

        .rcs(domain.rcs)
        .siret(domain.siret)
        .numDuns(domain.numDuns)
        .numTva(domain.numTva)
        .codeNaf(domain.codeNaf)
        .appartenanceGroupe(domain.appartenanceGroupe)
        .typeEntreprise(domain.typeEntreprise)

        .adminContact(domain.adminContact?.let { toModel(it) })
        .bankInfo(domain.bankInfo?.let { toModel(it) })
        .fiscalAddress(domain.fiscalAddress?.let { toModel(it) })

//        .kbisFilename(domain.kbisFilename)
//        .ribFilename(domain.ribFilename)
//        .rcFilename(domain.rcFilename)
//        .urssafFilename(domain.urssafFilename)
//        f.iscalFilename(domain.fiscalFilename)

        .state(domain.state?.let { toModel(it) })
        .lastDate(domain.lastDate)
        .build()
}

fun fromModel(model: CompanyModel) : CompanyDomain {
    return CompanyDomain.Builder("......")
        .raisonSocial(model.raisonSocial)
        .nomCommercial(model.nomCommercial)
        .formeJuridique(model.formeJuridique)
        .capital(model.capital)

        .rcs(model.rcs)
        .siret(model.siret)
        .numDuns(model.numDuns)
        .numTva(model.numTva)
        .codeNaf(model.codeNaf)
        .appartenanceGroupe(model.appartenanceGroupe)
        .typeEntreprise(model.typeEntreprise)

        .adminContact(model.adminContact?.let { fromModel(it) })
        .bankInfo(model.bankInfo?.let { fromModel(it) })
        .fiscalAddress(model.fiscalAddress?.let { fromModel(it) })

//        .kbisFilename(model.kbisFilename)
//        .ribFilename(model.ribFilename)
//        .rcFilename(model.rcFilename)
//        .urssafFilename(model.urssafFilename)
//        f.iscalFilename(model.fiscalFilename)

        .state(model.state?.let { fromModel(it) })
        .lastDate(model.lastDate)
        .build()
}