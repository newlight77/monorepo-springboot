package io.tricefal.core.account.model

import io.tricefal.core.account.domain.*
import java.time.Instant

data class AccountModel(var id: Long,
                        val username: String,
                        val expirationDate: Instant,
                        val contact: ContactModel,
                        val address: AddressModel,
                        val company: CompanyModel,
                        val fiscalAddress: AddressModel,
                        val privacyDetail: PrivacyDetailModel)

fun fromDomain(domain: AccountDomain): AccountModel {
    return AccountModel(
            domain.id,
            domain.username,
            domain.expirationDate,
            fromDomain(domain.contact),
            fromDomain(domain.address),
            fromDomain(domain.company),
            fromDomain(domain.fiscalAddress),
            fromDomain(domain.privacyDetail)
    )
}

fun toDomain(model: AccountModel): AccountDomain {
    return AccountDomain(
            model.id,
            model.username,
            model.expirationDate,
            toDomain(model.contact),
            toDomain(model.address),
            toDomain(model.company),
            toDomain(model.fiscalAddress),
            toDomain(model.privacyDetail)
    )
}