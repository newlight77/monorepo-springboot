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
                iban,
                owner,
                bic,
                address
        )
    }
}

fun toModel(domain: BankInfoDomain): BankInfoModel {
    return BankInfoModel(
            domain.iban,
            domain.owner,
            domain.bic,
            domain.address?.let { toModel(it) }
    )
}

fun fromModel(model: BankInfoModel) : BankInfoDomain {
    return BankInfoDomain(
            model.iban,
            model.owner,
            model.bic,
            model.address?.let { fromModel(it) }
    )
}