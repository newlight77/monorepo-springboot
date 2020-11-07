package io.tricefal.core.freelance

data class BankInfoModel(
        val iban:  String,
        val owner: String?,
        val bic: String?,
        val address: AddressModel?
) {
    data class Builder(
            val iban: String,
            var owner: String? = null,
            var bic: String? = null,
            var address: AddressModel? = null
    ) {
        fun owner(owner: String?) = apply { this.owner = owner }
        fun bic(bic: String?) = apply { this.bic = bic }
        fun address(address: AddressModel?) = apply { this.address = address }

        fun build() = BankInfoModel(
                iban = iban,
                owner = owner,
                bic = bic,
                address = address
        )
    }
}

fun toModel(domain: BankInfoDomain): BankInfoModel {
    return BankInfoModel(
            iban = domain.iban,
            owner = domain.owner,
            bic = domain.bic,
            address = domain.address?.let { toModel(it) }
    )
}

fun fromModel(model: BankInfoModel) : BankInfoDomain {
    return BankInfoDomain(
            iban = model.iban,
            owner = model.owner,
            bic = model.bic,
            address = model.address?.let { fromModel(it) }
    )
}