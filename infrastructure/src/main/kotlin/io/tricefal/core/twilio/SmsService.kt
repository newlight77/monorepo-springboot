package io.tricefal.core.twilio

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import org.springframework.stereotype.Service

@Service
class SmsService(twilioProperties: TwilioProperties) {

    init {
        Twilio.init(twilioProperties.accountSid, twilioProperties.authToken)
    }

    fun send(message: SmsMessage): String {
        return Message.creator(
                PhoneNumber(message.to),
                PhoneNumber(message.from),
                message.content)
            .create().sid
    }
}