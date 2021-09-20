package io.oneprofile.signup.company

import java.time.Instant

class CompanyDomain(
    val raisonSocial: String,
    val nomCommercial: String?,
    val formeJuridique: String?,
    val capital: String?,
    val rcs: String?,
    val siret: String?,
    val numDuns: String?,
    val numTva: String?,
    val codeNaf: String?,
    var companyCreationDate: Instant?,
    var companyUpdateDate: Instant?,

    var pdgContact: ContactDomain?,
    var pdgPrivacyDetail: PrivacyDetailDomain? = null,
    var adminContact: ContactDomain?,
    var bankInfo: BankInfoDomain?,
    var fiscalAddress: AddressDomain?,
    var motherCompany: MotherCompanyDomain?,
    var documents: CompanyDocumentsDomain?,

    var state: CompanyStateDomain?,
    var lastDate: Instant?
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

        var pdgContact: ContactDomain? = null,
        var pdgPrivacyDetail: PrivacyDetailDomain? = null,
        var adminContact: ContactDomain? = null,
        var bankInfo: BankInfoDomain? = null,
        var fiscalAddress: AddressDomain? = null,
        var motherCompany: MotherCompanyDomain? = null,
        var documents: CompanyDocumentsDomain? = null,

        var state: CompanyStateDomain? = null,
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

        fun pdgContact(pdgContact: ContactDomain?) = apply { this.pdgContact = pdgContact }
        fun pdgPrivacyDetail(pdgPrivacyDetail: PrivacyDetailDomain?) = apply { this.pdgPrivacyDetail = pdgPrivacyDetail }
        fun adminContact(adminContact: ContactDomain?) = apply { this.adminContact = adminContact }
        fun bankInfo(bankInfo: BankInfoDomain?) = apply { this.bankInfo = bankInfo }
        fun fiscalAddress(fiscalAddress: AddressDomain?) = apply { this.fiscalAddress = fiscalAddress }
        fun motherCompany(motherCompany: MotherCompanyDomain?) = apply { this.motherCompany = motherCompany }
        fun documents(documents: CompanyDocumentsDomain?) = apply { this.documents = documents }

        fun state(state: CompanyStateDomain?) = apply { this.state = state }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

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
            companyCreationDate = companyCreationDate,
            companyUpdateDate = companyUpdateDate,

            pdgContact = pdgContact,
            pdgPrivacyDetail = pdgPrivacyDetail,
            adminContact = adminContact,
            bankInfo = bankInfo,
            fiscalAddress = fiscalAddress,
            motherCompany = motherCompany,
            documents = documents,

            state = state,
            lastDate = lastDate
        )
    }
}