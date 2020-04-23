package io.tricefal.core.account.entity

import io.tricefal.core.account.domain.BankInfoDomain
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "bank_info")
data class BankInfoEntity(

        @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
        @SequenceGenerator(name = "sequenceGenerator")
        val id: Long,

        @javax.persistence.Column(name = "owner", length = 50)
        val owner: @Size(max = 50) String,

        @OneToOne
        @JoinColumn(name = "address")
        val address: AddressEntity,

        @Column(name = "iban", length = 50)
        val iban: @Size(max = 50) String,

        @Column(name = "bic", length = 50)
        val bic: @Size(max = 50) String,

        @Column(name = "rib_url", length = 256)
        val ribUrl: @Size(max = 256) String
)

fun fromDomain(domain: BankInfoDomain): BankInfoEntity {
        return BankInfoEntity(
                domain.id,
                domain.owner,
                fromDomain(domain.address),
                domain.iban,
                domain.bic,
                domain.ribUrl
        )
}

fun toDomain(entity: BankInfoEntity) : BankInfoDomain{
        return BankInfoDomain(
                entity.id,
                entity.owner,
                toDomain(entity.address),
                entity.iban,
                entity.bic,
                entity.ribUrl
        )
}