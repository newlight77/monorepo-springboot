package io.tricefal.core.mail

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
data class MailConfig(
    @Value("\${mail.from}")
    var from: String
)