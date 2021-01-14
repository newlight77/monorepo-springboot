package io.tricefal.core.freelance

import java.time.Instant

data class BankInfoDomain(
    val iban:  String?,
    val owner: String?,
    val bic: String?,
    var address: AddressDomain?,
    val lastDate: Instant?
) {
    data class Builder(
        var iban: String? = null,
        var owner: String? = null,
        var bic: String? = null,
        var address: AddressDomain? = null,
        var lastDate: Instant? = null
    ) {
        fun iban(iban: String?) = apply { this.iban = iban }
        fun owner(owner: String?) = apply { this.owner = owner }
        fun bic(bic: String?) = apply { this.bic = bic }
        fun address(address: AddressDomain?) = apply { this.address = address }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = BankInfoDomain(
                iban = iban,
                owner = owner,
                bic = bic,
                address = address,
                lastDate
        )
    }
}
