package io.tricefal.core.freelance

data class BankInfoDomain(
        val iban:  String,
        val owner: String?,
        val bic: String?,
        val address: AddressDomain?
) {
    data class Builder(
            val iban: String,
            var owner: String? = null,
            var bic: String? = null,
            var address: AddressDomain? = null
    ) {
        fun owner(owner: String?) = apply { this.owner = owner }
        fun bic(bic: String?) = apply { this.bic = bic }
        fun address(address: AddressDomain?) = apply { this.address = address }

        fun build() = BankInfoDomain(
                iban,
                owner,
                bic,
                address
        )
    }
}
