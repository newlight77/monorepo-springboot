package io.tricefal.core.account.entity

import io.tricefal.core.account.domain.CompanyDomain
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "company")
data class CompanyEntity(
        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
        @SequenceGenerator(name = "sequenceGenerator")
        var id: Long,

        @Column(name = "raison_social", length = 50)
        val raisonSocial: @Size(max = 50) String,

        @Column(name = "nom_commercial", length = 50)
        val nomCommercial: @Size(max = 50) String,

        @Column(name = "forme_juridique", length = 50)
        val formeJuridique: @Size(max = 50) String,

        @Column(name = "capital", length = 50)
        val capital: @Size(max = 50) String,

        @Column(name = "rcs", length = 50)
        val rcs: @Size(max = 50) String,

        @Column(name = "siret", length = 50)
        val siret: @Size(max = 50) String,

        @Column(name = "num_duns", length = 50)
        val numDuns: @Size(max = 50) String,

        @Column(name = "num_tva", length = 50)
        val numTva: @Size(max = 50) String,

        @Column(name = "code_naf", length = 50)
        val codeNaf: @Size(max = 50) String,

        @Column(name = "appartenance_groupe", length = 50)
        val appartenanceGroupe: @Size(max = 50) String,

        @Column(name = "type_entreprise", length = 50)
        val typeEntreprise: @Size(max = 50) String,

        @Column(name = "kbis_url", length = 256)
        val kbisUrl: @Size(max = 256) String,

        @Column(name = "rib_url", length = 256)
        val ribUrl: @Size(max = 256) String,

        @Column(name = "rc_url", length = 256)
        val rcUrl: @Size(max = 256) String) {

        @OneToOne
        @JoinColumn(name = "contact_id")
        lateinit var contact: ContactEntity

        @OneToOne
        @JoinColumn(name = "bank_info_id")
        lateinit var bankInfo: BankInfoEntity
}

fun fromDomain(domain: CompanyDomain): CompanyEntity {
        var entity = CompanyEntity(
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
                domain.rcUrl
        )
        entity.contact = fromDomain(domain.contact)
        entity.bankInfo = fromDomain(domain.bankInfo)

        return entity;
}

fun toDomain(entity: CompanyEntity): CompanyDomain {
        return CompanyDomain(
                entity.id,
                entity.raisonSocial,
                entity.nomCommercial,
                entity.formeJuridique,
                entity.capital,
                entity.rcs,
                entity.siret,
                entity.numDuns,
                entity.numTva,
                entity.codeNaf,
                entity.appartenanceGroupe,
                entity.typeEntreprise,
                entity.kbisUrl,
                entity.ribUrl,
                entity.rcUrl,
                toDomain(entity.contact),
                toDomain(entity.bankInfo)
        )
}