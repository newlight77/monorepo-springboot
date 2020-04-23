package io.tricefal.core.account.entity

import io.tricefal.core.account.domain.AccountDomain
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "account")
data class AccountEntity(
                @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
                @SequenceGenerator(name = "sequenceGenerator")
                @Id var id: Long,
                @Column(name = "nom", length = 50)
                var username: @Size(max = 50) String = "",

                @Column(name = "expiration_date")
                var expirationDate: Instant
) {

        @OneToOne
        @JoinColumn(name = "contact")
        lateinit var contact: ContactEntity

        @OneToOne
        @JoinColumn(name = "primary_address")
        lateinit var address: AddressEntity

        @OneToOne
        @JoinColumn(name = "company_id")
        lateinit var company: CompanyEntity

        @OneToOne
        @JoinColumn(name = "fiscal_address")
        lateinit var fiscalAddress: AddressEntity

        @OneToOne
        @JoinColumn(name = "privacy_detail")
        lateinit var privacyDetail: PrivacyDetailEntity
}

fun fromDomain(domain: AccountDomain): AccountEntity {
        var entity = AccountEntity(domain.id, domain.username, domain.expirationDate)
        entity.contact = fromDomain(domain.contact)
        entity.address = fromDomain(domain.address)
        entity.company = fromDomain(domain.company)
        entity.fiscalAddress = fromDomain(domain.fiscalAddress)
        entity.privacyDetail = fromDomain(domain.privacyDetail)
        return entity
}

fun toDomain(entity: AccountEntity): AccountDomain {
        return AccountDomain(
                entity.id,
                entity.username,
                entity.expirationDate,
                toDomain(entity.contact),
                toDomain(entity.address),
                toDomain(entity.company),
                toDomain(entity.fiscalAddress),
                toDomain(entity.privacyDetail)
        );
}
