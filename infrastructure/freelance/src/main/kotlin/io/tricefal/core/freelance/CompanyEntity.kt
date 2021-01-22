package io.tricefal.core.freelance

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "company")
data class CompanyEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @Column(name = "raison_social", length = 100)
        @Size(min = 3, max = 100)
        val raisonSocial: String,

        @Column(name = "nom_commercial", length = 100)
        @Size(min = 3, max = 100)
        val nomCommercial: String? = null,

        @Column(name = "forme_juridique", length = 50)
        val formeJuridique: String? = null,

        @Column(name = "capital", length = 100)
        val capital: String? = null,

        @Column(name = "rcs", length = 100)
        val rcs: String? = null,

        @Column(name = "siret", length = 100)
        val siret: String? = null,

        @Column(name = "num_duns", length = 100)
        val numDuns: String? = null,

        @Column(name = "num_tva", length = 100)
        val numTva: String? = null,

        @Column(name = "code_naf", length = 100)
        val codeNaf: String? = null,

        @Column(name = "cie_creation_date")
        var companyCreationDate: Instant? = Instant.now(),

        @Column(name = "cie_update_date")
        var companyUpdateDate: Instant? = Instant.now(),

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "pdg_contact_id")
        var pdgContact: ContactEntity? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "pdg_privacy_id")
        var pdgPrivacyDetail: PrivacyDetailEntity? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "admin_contact_id")
        var adminContact: ContactEntity? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "bank_info_id")
        var bankInfo: BankInfoEntity? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "fiscal_address_id")
        var fiscalAddress: AddressEntity? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "documents_id")
        var documents: CompanyDocumentsEntity? = null,

        @Column(name = "mother_name", length = 100)
        val motherRaisonSocial: String? = null,

        @Column(name = "mother_type", length = 100)
        val motherTypeEntreprise: String? = null,

        @Column(name = "mother_capital", length = 100)
        val motherCapital: String? = null,

        @Column(name = "mother_creat_date", length = 100)
        val motherCreationDate: Instant? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "state")
        var state: CompanyStateEntity? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = Instant.now()

)

fun toEntity(domain: CompanyDomain): CompanyEntity {
        return CompanyEntity(
                id = null,
                raisonSocial = domain.raisonSocial,
                nomCommercial = domain.nomCommercial,
                formeJuridique = domain.formeJuridique,
                capital = domain.capital,
                rcs = domain.rcs,
                siret = domain.siret,
                numDuns =  domain.numDuns,
                numTva = domain.numTva,
                codeNaf = domain.codeNaf,
                companyCreationDate = domain.companyCreationDate,
                companyUpdateDate = domain.companyUpdateDate,
                pdgContact = domain.pdgContact?.let { toEntity(it) },
                pdgPrivacyDetail = domain.pdgPrivacyDetail?.let { toEntity(it) },
                adminContact = domain.adminContact?.let { toEntity(it) },
                bankInfo = domain.bankInfo?.let { toEntity(it) },
                fiscalAddress = domain.fiscalAddress?.let { toEntity(it) },
                documents = domain.documents?.let { toEntity(it) },
                motherRaisonSocial = domain.motherCompany?.raisonSocial,
                motherTypeEntreprise = domain.motherCompany?.typeEntreprise,
                motherCapital = domain.motherCompany?.capital,
                motherCreationDate = domain.motherCompany?.creationDate,
                state = domain.state?.let { toEntity(it) },
                lastDate = domain.lastDate
        )
}

fun fromEntity(entity: CompanyEntity): CompanyDomain {
        return CompanyDomain(
                raisonSocial = entity.raisonSocial,
                nomCommercial = entity.nomCommercial,
                formeJuridique = entity.formeJuridique,
                capital = entity.capital,
                rcs = entity.rcs,
                siret = entity.siret,
                numDuns = entity.numDuns,
                numTva = entity.numTva,
                codeNaf = entity.codeNaf,
                companyCreationDate = entity.companyCreationDate,
                companyUpdateDate = entity.companyUpdateDate,
                pdgContact = entity.pdgContact?.let { fromEntity(it) },
                pdgPrivacyDetail = entity.pdgPrivacyDetail?.let { fromEntity(it) },
                adminContact = entity.adminContact?.let { fromEntity(it) },
                bankInfo = entity.bankInfo?.let { fromEntity(it) },
                fiscalAddress = entity.fiscalAddress?.let { fromEntity(it) },
                documents = entity.documents?.let { fromEntity(it) },
                motherCompany = MotherCompanyDomain(
                        entity.motherRaisonSocial,
                        entity.motherTypeEntreprise,
                        entity.motherCapital,
                        entity.motherCreationDate
                ),
                state = entity.state?.let { fromEntity(it) },
                lastDate = entity.lastDate
        )
}