package io.oneprofile.signup.company

import java.time.Instant

data class BankInfoModel(
        val iban:  String?,
        val owner: String?,
        val bic: String?,
        val address: AddressModel?,
        var lastDate: Instant?
) {
    data class Builder(
            var iban: String? = null,
            var owner: String? = null,
            var bic: String? = null,
            var address: AddressModel? = null,
            var lastDate: Instant? = null
    ) {
        fun iban(iban: String?) = apply { this.iban = iban }
        fun owner(owner: String?) = apply { this.owner = owner }
        fun bic(bic: String?) = apply { this.bic = bic }
        fun address(address: AddressModel?) = apply { this.address = address }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = BankInfoModel(
                iban = iban,
                owner = owner,
                bic = bic,
                address = address,
                lastDate = lastDate
        )
    }
}

fun toModel(domain: BankInfoDomain): BankInfoModel {
    return BankInfoModel(
            iban = domain.iban,
            owner = domain.owner,
            bic = domain.bic,
            address = domain.address?.let { toModel(it) },
            lastDate = domain.lastDate
    )
}

fun fromModel(model: BankInfoModel) : BankInfoDomain {
    return BankInfoDomain(
            iban = model.iban,
            owner = model.owner,
            bic = model.bic,
            address = model.address?.let { fromModel(it) },
            lastDate = model.lastDate
    )
}