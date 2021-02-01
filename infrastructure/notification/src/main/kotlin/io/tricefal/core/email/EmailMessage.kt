package io.tricefal.core.email

import java.io.InputStream

class EmailMessage(var from: String,
                   val to: String,
                   val subject: String,
                   val content: String,
                   val cc: String? = null,
                   val bcc: String? = null,
                   var attachment: InputStream? = null,
                   var emailTemplate: EmailTemplate? = null,
                   var model: Map<String, String>? = null) {

    constructor() : this(from = "",
                        to = "",
                        cc = "",
                        bcc = "",
                        subject = "",
                        content= "")

    class Builder {
        private lateinit var from: String
        private lateinit var to: String
        private lateinit var subject: String
        private lateinit var content: String
        private var cc: String? = null
        private var bcc: String? = null
        private var attachment: InputStream? = null
        private var emailTemplate: EmailTemplate? = null
        private var model: Map<String, String>? = null

        fun from(from: String) =  apply { this.from = from }
        fun to(to: String) =  apply { this.to = to }
        fun subject(subject: String) =  apply { this.subject = subject }
        fun content(content: String) =  apply { this.content = content }
        fun cc(cc: String?) =  apply { this.cc = cc }
        fun bcc(bcc: String?) =  apply { this.bcc = bcc }
        fun attachment(attachment: InputStream?) =  apply { this.attachment = attachment }
        fun emailTemplate(emailTemplate: EmailTemplate?) =  apply { this.emailTemplate = emailTemplate }
        fun model(model: Map<String, String>?) =  apply { this.model = model }

        fun build(): EmailMessage { return EmailMessage(
                from = from,
                to = to,
                cc = cc,
                bcc = bcc,
                subject = subject,
                content = content,
                attachment = attachment,
                emailTemplate = emailTemplate,
                model = model
        )}
    }

}

enum class EmailTemplate(val filename: String) {
    SIGNUP("email-template.html"),
    SIGNUP_DEV("email-template-dev.html");
}

fun emailTemplateByEnv(env: String) = mapOf(
    "" to EmailTemplate.SIGNUP,
    "prod" to EmailTemplate.SIGNUP,
    "dev" to EmailTemplate.SIGNUP_DEV,
)[env]

enum class EmailTemplateThemeStyle(val style: String) {
    PROD(""),
    DEV(".greeting { color: #FFD500;} " +
            ".content { color: #FFD500;}" +
            ".signature { color: #FFD500;}")
}

fun emailTemplateThemeStyleByEnv(env: String) = mapOf(
    "" to EmailTemplateThemeStyle.PROD,
    "prod" to EmailTemplateThemeStyle.PROD,
    "dev" to EmailTemplateThemeStyle.DEV,
)[env]

fun emailSubjectByEnv(env: String) = mapOf(
    "" to "",
    "prod" to "",
    "dev" to " *** DEV ***",
)[env]