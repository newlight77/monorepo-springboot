package io.tricefal.core.freelance

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "company")
data class CompanyEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long?,

        @Column(name = "raison_social", length = 50)
        val raisonSocial: @Size(max = 50) String,

        @Column(name = "nom_commercial", length = 50)
        val nomCommercial: String? = null,

        @Column(name = "forme_juridique", length = 50)
        val formeJuridique: @Size(max = 50) String? = null,

        @Column(name = "capital", length = 50)
        val capital: String? = null,

        @Column(name = "rcs", length = 50)
        val rcs: String? = null,

        @Column(name = "siret", length = 50)
        val siret: String? = null,

        @Column(name = "num_duns", length = 50)
        val numDuns: String? = null,

        @Column(name = "num_tva", length = 50)
        val numTva: String? = null,

        @Column(name = "code_naf", length = 50)
        val codeNaf: String? = null,

        @Column(name = "appartenance_groupe", length = 50)
        val appartenanceGroupe: String? = null,

        @Column(name = "type_entreprise", length = 50)
        val typeEntreprise: @Size(max = 50) String? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "admin_contact_id")
        var adminContact: ContactEntity? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "bank_info_id")
        var bankInfo: BankInfoEntity? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "fiscal_address_id")
        var fiscalAddress: AddressEntity? = null
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
                appartenanceGroupe = domain.appartenanceGroupe,
                typeEntreprise =  domain.typeEntreprise,
                adminContact = domain.adminContact?.let { toEntity(it) },
                bankInfo = domain.bankInfo?.let { toEntity(it) },
                fiscalAddress = domain.fiscalAddress?.let { toEntity(it) }
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
                appartenanceGroupe = entity.appartenanceGroupe,
                typeEntreprise = entity.typeEntreprise,
                adminContact = entity.adminContact?.let { fromEntity(it) },
                bankInfo = entity.bankInfo?.let { fromEntity(it) },
                fiscalAddress = entity.fiscalAddress?.let { fromEntity(it) }
        )
}