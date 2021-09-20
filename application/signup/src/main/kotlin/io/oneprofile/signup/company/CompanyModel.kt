package io.oneprofile.signup.company

import java.time.Instant

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
        var companyCreationDate: Instant? = null,
        var companyUpdateDate: Instant? = null,

        var pdgContact: ContactModel? = null,
        var pdgPrivacyDetail: PrivacyDetailModel? = null,
        var adminContact: ContactModel? = null,
//        var bankInfo: BankInfoModel? = null,
        var fiscalAddress: AddressModel? = null,
        var motherCompany: MotherCompanyModel? = null, // optional
        var documents: CompanyDocumentsModel? = null, // optional

        var state: CompanyStateModel? = null,
        var lastDate: Instant? = null
) {
    data class Builder(
            var raisonSocial: String,
            var nomCommercial: String? = null,
            var formeJuridique: String? = null,
            var capital: String? = null,

            var rcs: String? = null,
            var siret: String? = null,
            var numDuns: String? = null,
            var numTva: String? = null,
            var codeNaf: String? = null,
            var companyCreationDate: Instant? = null,
            var companyUpdateDate: Instant? = null,

            var pdgContact: ContactModel? = null,
            var pdgPrivacyDetail: PrivacyDetailModel? = null,
            var adminContact: ContactModel? = null,
//            var bankInfo: BankInfoModel? = null,
            var fiscalAddress: AddressModel? = null,
            var motherCompany: MotherCompanyModel? = null,
            var documents: CompanyDocumentsModel? = null,

            var state: CompanyStateModel? = null,
            var lastDate: Instant? = null
    ) {
//        fun raisonSocial(raisonSocial: String?) = apply { this.raisonSocial = raisonSocial }
        fun nomCommercial(nomCommercial: String?) = apply { this.nomCommercial = nomCommercial }
        fun formeJuridique(formeJuridique: String?) = apply { this.formeJuridique = formeJuridique }
        fun capital(capital: String?) = apply { this.capital = capital }
        fun rcs(rcs: String?) = apply { this.rcs = rcs }
        fun siret(siret: String?) = apply { this.siret = siret }
        fun numDuns(numDuns: String?) = apply { this.numDuns = numDuns }
        fun numTva(numTva: String?) = apply { this.numTva = numTva }
        fun codeNaf(codeNaf: String?) = apply { this.codeNaf = codeNaf }
        fun companyCreationDate(companyCreationDate: Instant?) = apply { this.companyCreationDate = companyCreationDate }
        fun companyUpdateDate(companyUpdateDate: Instant?) = apply { this.companyUpdateDate = companyUpdateDate }
        
        fun pdgContact(pdgContact: ContactModel?) = apply { this.pdgContact = pdgContact }
        fun pdgPrivacyDetail(pdgPrivacyDetail: PrivacyDetailModel?) = apply { this.pdgPrivacyDetail = pdgPrivacyDetail }
        fun adminContact(adminContact: ContactModel?) = apply { this.adminContact = adminContact }
//        fun bankInfo(bankInfo: BankInfoModel?) = apply { this.bankInfo = bankInfo }
        fun fiscalAddress(fiscalAddress: AddressModel?) = apply { this.fiscalAddress = fiscalAddress }
        fun motherCompany(motherCompany: MotherCompanyModel?) = apply { this.motherCompany = motherCompany }
        fun documents(documents: CompanyDocumentsModel?) = apply { this.documents = documents }

        fun state(state: CompanyStateModel?) = apply { this.state = state }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = CompanyModel(
            raisonSocial = raisonSocial,
            nomCommercial = nomCommercial,
            formeJuridique = formeJuridique,
            capital = capital,

            rcs = rcs,
            siret = siret,
            numDuns = numDuns,
            numTva = numTva,
            codeNaf = codeNaf,
            companyCreationDate = companyCreationDate,
            companyUpdateDate = companyUpdateDate,

            pdgContact = pdgContact,
            pdgPrivacyDetail = pdgPrivacyDetail,
            adminContact = adminContact,
//            bankInfo = bankInfo,
            fiscalAddress = fiscalAddress,
            motherCompany = motherCompany,
            documents = documents,

            state = state,
            lastDate = lastDate
        )
    }
}

fun toModel(domain: CompanyDomain) : CompanyModel {
    return CompanyModel.Builder(domain.raisonSocial)
        .nomCommercial(domain.nomCommercial)
        .formeJuridique(domain.formeJuridique)
        .capital(domain.capital)

        .rcs(domain.rcs)
        .siret(domain.siret)
        .numDuns(domain.numDuns)
        .numTva(domain.numTva)
        .codeNaf(domain.codeNaf)
        .companyCreationDate(domain.companyCreationDate)
        .companyUpdateDate(domain.companyUpdateDate)

        .pdgContact(domain.pdgContact?.let { toModel(it) })
        .pdgPrivacyDetail(domain.pdgPrivacyDetail?.let { toModel(it) })
        .adminContact(domain.adminContact?.let { toModel(it) })
//        .bankInfo(domain.bankInfo?.let { toModel(it) })
        .fiscalAddress(domain.fiscalAddress?.let { toModel(it) })
        .motherCompany(domain.motherCompany?.let { toModel(it) })
        .documents(domain.documents?.let { toModel(it) })

        .state(domain.state?.let { toModel(it) })
        .lastDate(domain.lastDate)
        .build()
}

fun fromModel(model: CompanyModel) : CompanyDomain {
    return CompanyDomain.Builder(model.raisonSocial)
        .nomCommercial(model.nomCommercial)
        .formeJuridique(model.formeJuridique)
        .capital(model.capital)

        .rcs(model.rcs)
        .siret(model.siret)
        .numDuns(model.numDuns)
        .numTva(model.numTva)
        .codeNaf(model.codeNaf)
        .companyCreationDate(model.companyCreationDate)
        .companyUpdateDate(model.companyUpdateDate)

        .pdgContact(model.pdgContact?.let { fromModel(it) })
        .pdgPrivacyDetail(model.pdgPrivacyDetail?.let { fromModel(it) })
        .adminContact(model.adminContact?.let { fromModel(it) })
//        .bankInfo(model.bankInfo?.let { fromModel(it) })
        .fiscalAddress(model.fiscalAddress?.let { fromModel(it) })
        .motherCompany(model.motherCompany?.let { fromModel(it) })
        .documents(model.documents?.let { fromModel(it) })

        .state(model.state?.let { fromModel(it) })
        .lastDate(model.lastDate)
        .build()
}