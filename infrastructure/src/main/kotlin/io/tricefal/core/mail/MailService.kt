package io.tricefal.core.mail

import org.slf4j.LoggerFactory
import org.springframework.core.io.InputStreamResource
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.MimeMessagePreparator
import org.springframework.stereotype.Service

@Service
class MailService(private val mailSender: JavaMailSender,
                  private val mailConfig: MailConfig) {

    fun send(mailMessage: MailMessage) {

        val messagePreparator = MimeMessagePreparator { mimeMessage ->
            val messageHelper = MimeMessageHelper(mimeMessage, true)
            messageHelper.setFrom(mailConfig.from)
            messageHelper.setTo(mailMessage.to)
            messageHelper.setSubject(mailMessage.subject)
            messageHelper.setText(mailMessage.content)
            mailMessage.attachment?.let {
                val file = InputStreamResource(it)
                messageHelper.addAttachment(file.filename!!, file) }
        }
        try {
            mailSender.send(messagePreparator)
            logger.info("Email sent successfully")
        } catch (e: MailException) {
            throw RuntimeException("An error occurred while sending an email", e)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MailService::class.java)
    }
}

