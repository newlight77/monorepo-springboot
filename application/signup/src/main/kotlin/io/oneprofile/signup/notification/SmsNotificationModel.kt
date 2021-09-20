package io.oneprofile.signup.notification

class SmsNotificationModel
private constructor(
        var smsFrom: String? = null,
        var smsTo: String? = null,
        var smsContent: String? = null
) {

    data class Builder (
            var smsFrom: String? = null,
            var smsTo: String? = null,
            var smsContent: String? = null
    ) {

        fun smsFrom(smsFrom: String?) = apply { this.smsFrom = smsFrom }
        fun smsTo(smsTo: String?) = apply { this.smsTo = smsTo }
        fun smsContent(smsContent: String?) = apply { this.smsContent = smsContent }

        fun build() = SmsNotificationModel(
                smsFrom = smsFrom,
                smsTo = smsTo,
                smsContent = smsContent
        )
    }
}

fun toModel(domain: SmsNotificationDomain): SmsNotificationModel {
    return SmsNotificationModel.Builder()
        .smsFrom(domain.smsFrom)
        .smsTo(domain.smsTo)
        .smsContent(domain.smsContent)
        .build()
}

fun fromModel(model: SmsNotificationModel): SmsNotificationDomain {
    return SmsNotificationDomain.Builder()
        .smsFrom(model.smsFrom)
        .smsTo(model.smsTo)
        .smsContent(model.smsContent)
        .build()
}