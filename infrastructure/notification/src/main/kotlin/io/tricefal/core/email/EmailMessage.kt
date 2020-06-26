package io.tricefal.core.email

import java.io.InputStream

class EmailMessage(var from: String,
                   val to: String,
                   val subject: String,
                   val content: String,
                   var attachment: InputStream? = null,
                   var emailTemplate: EmailTemplate? = null,
                   var model: Map<String, String>? = null) {

    constructor() : this(from = "",
                        to = "",
                        subject = "",
                        content= "")

    class Builder {
        private lateinit var from: String
        private lateinit var to: String
        private lateinit var subject: String
        private lateinit var content: String
        private var attachment: InputStream? = null
        private var emailTemplate: EmailTemplate? = null
        private var model: Map<String, String>? = null

        fun from(from: String) =  apply { this.from = from }
        fun to(to: String) =  apply { this.to = to }
        fun subject(subject: String) =  apply { this.subject = subject }
        fun content(content: String) =  apply { this.content = content }
        fun attachment(attachment: InputStream) =  apply { this.attachment = attachment }
        fun emailTemplate(emailTemplate: EmailTemplate) =  apply { this.emailTemplate = emailTemplate }
        fun model(model: Map<String, String>) =  apply { this.model = model }

        fun build(): EmailMessage { return EmailMessage(from, to, subject, content, attachment, emailTemplate, model)}
    }

}

enum class EmailTemplate(val filename: String) {
    SIGNUP("email-template.html")
}