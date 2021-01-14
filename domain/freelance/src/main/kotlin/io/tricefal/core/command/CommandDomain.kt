package io.tricefal.core.command

import io.tricefal.core.freelance.AddressDomain
import io.tricefal.core.freelance.ContactDomain
import java.time.Instant

class CommandDomain(
    val companyName: String,
    var address: AddressDomain?,
    var contact: ContactDomain?,
    var paymentConditionDays: Int?,
    var paymentMode: String?,
    var lastDate: Instant?
) {
    data class Builder(
        val companyName: String,
        var address: AddressDomain? = null,
        var contact: ContactDomain? = null,
        var paymentConditionDays: Int? = null,
        var paymentMode: String? = null,
        var lastDate: Instant? = null
    ) {
        fun address(address: AddressDomain?) = apply { this.address = address }
        fun contact(contact: ContactDomain?) = apply { this.contact = contact }
        fun paymentConditionDays(paymentConditionDays: Int?) = apply { this.paymentConditionDays = paymentConditionDays }
        fun paymentMode(paymentMode: String?) = apply { this.paymentMode = paymentMode }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = CommandDomain(
            companyName = companyName,
            address = address,
            contact = contact,
            paymentConditionDays = paymentConditionDays,
            paymentMode = paymentMode,
            lastDate = lastDate
        )
    }
}