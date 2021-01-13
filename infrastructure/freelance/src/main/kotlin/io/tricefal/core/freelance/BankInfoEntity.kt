package io.tricefal.core.freelance

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "bank_info")
data class BankInfoEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @Column(name = "iban", length = 50)
        val iban: @Size(max = 50) String? = null,

        @javax.persistence.Column(name = "owner", length = 50)
        val owner: @Size(max = 50) String? = null,

        @Column(name = "bic", length = 50)
        val bic: @Size(max = 50) String? =  null,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "address")
        val address: AddressEntity? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = Instant.now()

)

fun toEntity(domain: BankInfoDomain): BankInfoEntity {
        return BankInfoEntity(
                id = null,
                iban = domain.iban,
                owner = domain.owner,
                bic = domain.bic,
                address =  domain.address?.let { toEntity(it) },
                lastDate = domain.lastDate
        )
}

fun fromEntity(entity: BankInfoEntity) : BankInfoDomain{
        return BankInfoDomain(
                iban = entity.iban,
                owner = entity.owner,
                bic = entity.bic,
                address = entity.address?.let { fromEntity(it) },
                lastDate = entity.lastDate
        )
}