package io.tricefal.core.signup

import io.tricefal.core.notification.MetaNotificationDomain
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant


@ExtendWith(MockitoExtension::class)
class SignupNotificationFactoryTest {

    @Spy
    lateinit var factory: SignupNotificationFactory

    @Test
    fun `should build an sms notification by substituting value within the sms template`() {
        // arrange
        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl", emailFrom = "emailFrom", smsFrom = "smsFrom")
        val signup = SignupDomain.Builder("kong@gmail.com")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .activationToken("token")
                .status(Status.FREELANCE)
                .state(SignupStateDomain.Builder("kong@gmail.com").build())
                .build()

        // Act
        val result = factory.smsNotification(signup, metaNotification)

        // assert
        Assertions.assertEquals("1234567890", result.smsTo)
        Assertions.assertEquals("Bonjour kong, le code d\'activation à saisir pour valider votre numéro de portable est 123456", result.smsContent)
    }

    @Test
    fun `should build an email notification by substituting value within the email template`() {
        // arrange
        val metaNotification = MetaNotificationDomain(baseUrl = "baseUrl", emailFrom = "emailFrom", smsFrom = "smsFrom")
        val signup = SignupDomain.Builder("kong@gmail.com")
                .firstname("kong")
                .lastname("to")
                .phoneNumber("1234567890")
                .signupDate(Instant.now())
                .activationCode("123456")
                .activationToken("token")
                .status(Status.FREELANCE)
                .state(SignupStateDomain.Builder("kong@gmail.com").build())
                .build()

//        Mockito.`when`(factory.emailValidationLink(signup, metaNotification)).thenReturn("link") // spy not mockable this way
//        Mockito.doReturn("link").`when`(factory).emailValidationLink(signup, metaNotification) //mock a spy method
        Mockito.doReturn("random").`when`(factory).randomString() //mock a spy method

        // Act
        val result = factory.emailNotification(signup, metaNotification)

        // assert
        Assertions.assertEquals("kong@gmail.com", result.emailTo)
        Assertions.assertEquals("Tricefal souscription", result.emailSubject)
        Assertions.assertEquals("Bonjour kong,\n\n", result.emailGreeting)
        Assertions.assertEquals("Merci d\'avoir fait la souscription. Un compte a été créé. \n\nNous vous invitons à valider votre adresse email via ce lien :\n baseUrl/signup/email/verify?token=token.random=123456", result.emailContent)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}