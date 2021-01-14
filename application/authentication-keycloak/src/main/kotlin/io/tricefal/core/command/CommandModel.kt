package io.tricefal.core.command

import io.tricefal.core.freelance.AddressModel
import io.tricefal.core.freelance.ContactModel
import java.time.Instant

data class CommandModel(
        val companyName:  String,
        val address: AddressModel?,
        val contact: ContactModel?,
        val paymentConditionDays: Int?,
        val paymentMode: String?,
        var lastDate: Instant?
) {
    data class Builder(
        var companyName: String,
        var address: AddressModel? = null,
        var contact: ContactModel? = null,
        var paymentConditionDays: Int? = null,
        var paymentMode: String? = null,
        var lastDate: Instant? = null
    ) {
        fun address(address: AddressModel?) = apply { this.address = address }
        fun contact(contact: ContactModel?) = apply { this.contact = contact }
        fun paymentConditionDays(paymentConditionDays: Int?) = apply { this.paymentConditionDays = paymentConditionDays }
        fun paymentMode(paymentMode: String?) = apply { this.paymentMode = paymentMode }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = CommandModel(
            companyName = companyName,
            address = address,
            contact = contact,
            paymentConditionDays = paymentConditionDays,
            paymentMode = paymentMode,
            lastDate = lastDate
        )
    }
}

fun toModel(domain: CommandDomain): CommandModel {
    return CommandModel(
        companyName = domain.companyName,
        address = domain.address?.let { io.tricefal.core.freelance.toModel(it) },
        contact = domain.contact?.let { io.tricefal.core.freelance.toModel(it) },
        paymentConditionDays = domain.paymentConditionDays,
        paymentMode = domain.paymentMode,
        lastDate = domain.lastDate
    )
}

fun fromModel(model: CommandModel) : CommandDomain {
    return CommandDomain(
        companyName = model.companyName,
        address = model.address?.let { io.tricefal.core.freelance.fromModel(it) },
        contact = model.contact?.let { io.tricefal.core.freelance.fromModel(it) },
        paymentConditionDays = model.paymentConditionDays,
        paymentMode = model.paymentMode,
        lastDate = model.lastDate
    )
}