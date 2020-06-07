package io.tricefal.core.email

import freemarker.template.Configuration
import freemarker.template.Template
import org.slf4j.LoggerFactory
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.MimeMessagePreparator
import org.springframework.stereotype.Service
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import java.nio.charset.StandardCharsets
import javax.mail.util.ByteArrayDataSource


@Service
class EmailService(private val emailSender: JavaMailSender,
                   private val freemarkerConfig: Configuration) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun send(emailMessage: EmailMessage) {
        val messagePreparator = prepareMessage(emailMessage)
        try {
            emailSender.send(messagePreparator)
            logger.info("Email sent successfully")
        } catch (e: MailException) {
            throw RuntimeException("An error occurred while sending an email", e)
        }
    }

    private fun prepareMessage(emailMessage: EmailMessage): MimeMessagePreparator {
        return MimeMessagePreparator { mimeMessage ->
            val isMultipart = emailMessage.attachment?.let { true } ?:false
            val messageHelper = MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name())
            messageHelper.setFrom(emailMessage.from)
            messageHelper.setTo(emailMessage.to)
            messageHelper.setSubject(emailMessage.subject)
            emailMessage.attachment?.let {
                val attachment = ByteArrayDataSource(it, "application/octet-stream")
                messageHelper.addAttachment("file", attachment)
            }
            emailMessage.emailTemplate?.let {
                messageHelper.setText(htmlFromTemplate(it, emailMessage), true)
            } ?: messageHelper.setText(emailMessage.content)
        }
    }

    private fun htmlFromTemplate(emailTemplate: EmailTemplate, emailMessage: EmailMessage): String {
        val template: Template = freemarkerConfig.getTemplate(emailTemplate.filename)
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, emailMessage.model!!)
    }
}

