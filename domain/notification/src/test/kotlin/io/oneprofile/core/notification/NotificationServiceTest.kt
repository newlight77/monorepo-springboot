package io.oneprofile.core.notification

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class NotifictionServiceTest {

    @Mock
    lateinit var adapter: INotificationAdapter

    lateinit var service: INotificationService

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(adapter)
    }

    @Test
    fun `should send an email for contact notification`() {
        // Arranges
        val notification = EmailContactNotificationDomain.Builder()
            .emailFrom("kong@gmail.com")
            .emailTo("kong@gmail.com")
            .emailContent("hello, I would like to know more")
            .build()

        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl", emailFrom = "emailFrom", smsFrom = "smsFrom")
        Mockito.`when`(adapter.sendEmail(toEmail(notification, metaNotification))).thenReturn(true)

        this.service = NotificationService(adapter)

        // Act
        val result = this.service.sendEmailContact(notification, metaNotification)

        // Arrange
        Assertions.assertTrue(result)
    }

    @Test
    fun `should send an email for feedback notification`() {
        // Arranges
        val notification = EmailFeedbackNotificationDomain.Builder()
            .emailFrom("kong@gmail.com")
            .emailTo("kong@gmail.com")
            .emailContent("hello, I would like to help")
            .build()

        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl", emailFrom = "emailFrom", smsFrom = "smsFrom")
        Mockito.`when`(adapter.sendEmail(toEmail(notification, metaNotification))).thenReturn(true)

        this.service = NotificationService(adapter)

        // Act
        val result = this.service.sendEmailFeedback(notification, metaNotification)

        // Arrange
        Assertions.assertTrue(result)
    }

    @Test
    fun `should send a sms notification`() {
        // Arranges
        val notification = SmsNotificationDomain.Builder()
            .smsFrom("6666666666")
            .smsTo("7777777777")
            .smsContent("hello, I would like to know more")
            .build()

        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl", emailFrom = "emailFrom", smsFrom = "smsFrom")
        Mockito.`when`(adapter.sendSms(notification)).thenReturn(true)

        this.service = NotificationService(adapter)

        // Act
        val result = this.service.sendSms(notification, metaNotification)

        // Arrange
        Assertions.assertTrue(result)
    }

}