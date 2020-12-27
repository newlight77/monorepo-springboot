package io.tricefal.core.notification

class SmsNotificationModel
private constructor(
        val username: String,
        var smsFrom: String? = null,
        var smsTo: String? = null,
        var smsContent: String? = null
) {

    data class Builder (
            val username: String,
            var smsFrom: String? = null,
            var smsTo: String? = null,
            var smsContent: String? = null
    ) {

        fun smsFrom(smsFrom: String?) = apply { this.smsFrom = smsFrom }
        fun smsTo(smsTo: String?) = apply { this.smsTo = smsTo }
        fun smsContent(smsContent: String?) = apply { this.smsContent = smsContent }

        fun build() = SmsNotificationModel(
                username = username,
                smsFrom = smsFrom,
                smsTo = smsTo,
                smsContent = smsContent
        )
    }
}

fun toModel(domain: SmsNotificationDomain): SmsNotificationModel {
    return SmsNotificationModel.Builder(domain.username)
        .smsFrom(domain.smsFrom)
        .smsTo(domain.smsTo)
        .smsContent(domain.smsContent)
        .build()
}

fun fromModel(model: SmsNotificationModel): SmsNotificationDomain {
    return SmsNotificationDomain.Builder(model.username)
        .smsFrom(model.smsFrom)
        .smsTo(model.smsTo)
        .smsContent(model.smsContent)
        .build()
}