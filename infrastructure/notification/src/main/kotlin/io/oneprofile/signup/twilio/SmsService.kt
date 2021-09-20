package io.oneprofile.signup.twilio

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service


@Service
@PropertySource("classpath:twilio.yml")
class SmsService(env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private final val phoneNumber: String? = env.getProperty("notification.sms.twilio.phoneNumber")
    private final val accountSid: String? = env.getProperty("notification.sms.twilio.accountSid")
    private final val authToken: String? = env.getProperty("notification.sms.twilio.authToken")

    init {
        Twilio.init(accountSid, authToken)
    }

    fun send(message: SmsMessage): String {

        try {
            val response = Message.creator(
                    PhoneNumber(message.to),
                    PhoneNumber(phoneNumber),
                    message.content)
                    .create()
            logger.info("SMS sent successfully")
            return response.sid
        } catch (e: Exception) {
            throw RuntimeException("An error occurred while sending a SMS", e)
        }
    }
}