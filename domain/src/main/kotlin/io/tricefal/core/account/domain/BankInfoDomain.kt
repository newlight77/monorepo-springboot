package io.tricefal.core.account.domain

data class BankInfoDomain(
        var id: Long,
        val owner: String,
        val address: AddressDomain,
        val iban:  String,
        val bic: String,
        val ribUrl: String
)
