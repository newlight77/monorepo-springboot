package io.tricefal.core.notification

class SmsNotificationDomain
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

        fun build() = SmsNotificationDomain(
                username = username,
                smsFrom = smsFrom,
                smsTo = smsTo,
                smsContent = smsContent
        )
    }
}