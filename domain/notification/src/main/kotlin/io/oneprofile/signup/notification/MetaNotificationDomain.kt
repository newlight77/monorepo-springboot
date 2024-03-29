package io.oneprofile.signup.notification

class MetaNotificationDomain (
        val targetEnv: String? = null,
        val baseUrl: String? = null,
        val emailToName: String? = null,
        val emailAdmin: String? = null,
        val emailFrom: String? = null,
        val smsAdminNumber: String? = null,
        val smsFrom: String? = null
)
