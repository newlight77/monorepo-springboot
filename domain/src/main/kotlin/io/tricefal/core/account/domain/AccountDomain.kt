package io.tricefal.core.account.domain

import java.time.Instant

data class AccountDomain(var id: Long,
                         val username: String,
                         val expirationDate: Instant,
                         val contact: ContactDomain,
                         val address: AddressDomain,
                         val company: CompanyDomain,
                         val fiscalAddress: AddressDomain,
                         val privacyDetail: PrivacyDetailDomain
)