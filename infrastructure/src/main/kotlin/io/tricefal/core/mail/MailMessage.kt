package io.tricefal.core.mail

import java.io.InputStream

data class MailMessage(val to: String,
                       val subject: String,
                       val content: String,
                       var attachment: InputStream? = null
)