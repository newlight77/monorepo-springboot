package io.tricefal.core.mail

import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.MimeMessagePreparator


class MailServiceTest {

    private val mailConfig = MailConfig("example@mail.com")

    private val sender: JavaMailSender = Mockito.mock(JavaMailSender::class.java)

    @Test
    fun `should send an email`() {
        // arrange
        val message = MailMessage("to@mail.com","subject", "content", attachment = null)

        val messagePreparator = MimeMessagePreparator { mimeMessage ->
            val messageHelper = MimeMessageHelper(mimeMessage, false)
            messageHelper.setFrom(mailConfig.from)
            messageHelper.setTo(message.to)
            messageHelper.setSubject(message.subject)
            messageHelper.setText(message.content)
        }

        Mockito.doNothing().`when`(sender).send(messagePreparator)

        val arg = ArgumentCaptor.forClass(MimeMessagePreparator::class.java)

        // act
        MailService(sender, mailConfig).send(message)

        // assert
        Mockito.verify(sender).send(arg.capture())
    }
}