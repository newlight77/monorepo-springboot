package io.tricefal.core.account.model

import io.tricefal.core.account.domain.BankInfoDomain

data class BankInfoModel(
        var id: Long,
        val owner: String,
        val address: AddressModel,
        val iban:  String,
        val bic: String,
        val ribUrl: String
)

fun fromDomain(domain: BankInfoDomain): BankInfoModel {
    return BankInfoModel(
            domain.id,
            domain.owner,
            fromDomain(domain.address),
            domain.iban,
            domain.bic,
            domain.ribUrl
    )
}

fun toDomain(model: BankInfoModel) : BankInfoDomain {
    return BankInfoDomain(
            model.id,
            model.owner,
            toDomain(model.address),
            model.iban,
            model.bic,
            model.ribUrl
    )
}