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

        @NotNull
        @Size(min = 3, max = 100)
        @Column(name = "company_name", length = 100)
        val companyName: String,

        @Column(name = "raison_social", length = 100)
        @Size(min = 3, max = 100)
        val raisonSocial: String? = null,

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

        @Column(name = "appartenance_groupe", length = 100)
        val appartenanceGroupe: String? = null,

        @Column(name = "type_entreprise", length = 100)
        val typeEntreprise: String? = null,

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
        @JoinColumn(name = "state")
        var state: CompanyStateEntity? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = Instant.now()

)

fun toEntity(domain: CompanyDomain): CompanyEntity {
        val companyName = if (domain.companyName.isNullOrEmpty()) domain.nomCommercial!! else domain.companyName
        return CompanyEntity(
                id = null,
                companyName = companyName,
                raisonSocial = domain.raisonSocial,
                nomCommercial = domain.nomCommercial,
                formeJuridique = domain.formeJuridique,
                capital = domain.capital,
                rcs = domain.rcs,
                siret = domain.siret,
                numDuns =  domain.numDuns,
                numTva = domain.numTva,
                codeNaf = domain.codeNaf,
                appartenanceGroupe = domain.appartenanceGroupe,
                typeEntreprise =  domain.typeEntreprise,
                adminContact = domain.adminContact?.let { toEntity(it) },
                bankInfo = domain.bankInfo?.let { toEntity(it) },
                fiscalAddress = domain.fiscalAddress?.let { toEntity(it) },
                state = domain.state?.let { toEntity(it) },
                lastDate = domain.lastDate
        )
}

fun fromEntity(entity: CompanyEntity): CompanyDomain {
        val companyName = if (entity.companyName.isNullOrEmpty()) entity.nomCommercial!! else entity.companyName
        return CompanyDomain(
                companyName = companyName,
                raisonSocial = entity.raisonSocial,
                nomCommercial = entity.nomCommercial,
                formeJuridique = entity.formeJuridique,
                capital = entity.capital,
                rcs = entity.rcs,
                siret = entity.siret,
                numDuns = entity.numDuns,
                numTva = entity.numTva,
                codeNaf = entity.codeNaf,
                appartenanceGroupe = entity.appartenanceGroupe,
                typeEntreprise = entity.typeEntreprise,
                adminContact = entity.adminContact?.let { fromEntity(it) },
                bankInfo = entity.bankInfo?.let { fromEntity(it) },
                fiscalAddress = entity.fiscalAddress?.let { fromEntity(it) },
                state = entity.state?.let { fromEntity(it) },
                lastDate = entity.lastDate
        )
}