package io.tricefal.core.twilio

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service


@Service
@PropertySource("classpath:twilio.yml")
class SmsService(private val env: Environment) {

    init {
        val phoneNumber = env.getProperty("notification.sms.twilio.phoneNumber")
        val accountSid = env.getProperty("notification.sms.twilio.accountSid")
        val authToken = env.getProperty("notification.sms.twilio.authToken")
        Twilio.init(accountSid, authToken)
    }

    fun send(message: SmsMessage): String {
        return Message.creator(
                PhoneNumber(message.to),
                PhoneNumber(message.from),
                message.content)
            .create().sid
    }
}