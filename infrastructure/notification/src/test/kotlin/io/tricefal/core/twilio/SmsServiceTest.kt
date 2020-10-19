package io.tricefal.core.twilio

import com.twilio.exception.ApiException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.core.env.Environment

internal class SmsServiceTest {

    @Mock
    val env: Environment = Mockito.mock(Environment::class.java)

//    @Test
    fun `should send a SMS with a twilio valid phone number`() {
        // arrange
        val message = SmsMessage.Builder()
                .from("33644601054")
                .to("0659401130")
                .content("hello Kong, test sms notif")
                .build()
        Mockito.`when`(env.getProperty("notification.sms.twilio.phoneNumber")).thenReturn("33644601054")
        Mockito.`when`(env.getProperty("notification.sms.twilio.accountSid")).thenReturn("AC497ba2b4c6a9ecf212ea611537e0be92")
        Mockito.`when`(env.getProperty("notification.sms.twilio.authToken")).thenReturn("56dfdab0b2c07ed9d861b4a182d10e00")

        // act
        val result = SmsService(env).send(message)

        // assert
        Assertions.assertEquals(34, result.length)
        Assertions.assertTrue(result.matches(Regex("^SM[a-z0-9]*")))
    }

    @Test
    fun `should not send a SMS with a twilio invalid phone number`() {
        // arrange
        val message = SmsMessage.Builder()
                .from("0659401130")
                .to("0659401130")
                .content("hello Kong, test sms notif")
                .build()
        Mockito.`when`(env.getProperty("notification.sms.twilio.phoneNumber")).thenReturn("0659401130")
        Mockito.`when`(env.getProperty("notification.sms.twilio.accountSid")).thenReturn("AC497ba2b4c6a9ecf212ea611537e0be92")
        Mockito.`when`(env.getProperty("notification.sms.twilio.authToken")).thenReturn("56dfdab0b2c07ed9d861b4a182d10e00")

        // act
        val exception: RuntimeException = assertThrows (RuntimeException::class.java) {
            SmsService(env).send(message)
        }

        // assert
        assertEquals("An error occurred while sending a SMS", exception.message)
    }
}
