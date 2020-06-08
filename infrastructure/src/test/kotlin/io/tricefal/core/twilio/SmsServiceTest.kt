package io.tricefal.core.twilio

import com.twilio.exception.ApiException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class SmsServiceTest {

    val twilioProperties = TwilioProperties("",
            "AC497ba2b4c6a9ecf212ea611537e0be92",
            "56dfdab0b2c07ed9d861b4a182d10e00")

    @Test
    fun `should not send a SMS with a twilio invalid phone number`() {
        // arrange
        val message = SmsMessage.Builder()
                .from("0650304940")
                .to("0650304940")
                .content("hello arnaud, notif")
                .build()

        // act
        val exception: ApiException = assertThrows (ApiException::class.java) {
            SmsService(twilioProperties).send(message)
        }

        // assert
        assertEquals("The 'To' number 0650304940 is not a valid phone number.", exception.message)
    }
}
