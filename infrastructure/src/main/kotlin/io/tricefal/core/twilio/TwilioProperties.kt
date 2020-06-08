package io.tricefal.core.twilio

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.PropertySource

@ConfigurationProperties(prefix = "notification.sms.twilio")
@PropertySource("twilio.yml")
class TwilioProperties(
        val phoneNumber: String,
        val accountSid: String,
        val authToken: String
)