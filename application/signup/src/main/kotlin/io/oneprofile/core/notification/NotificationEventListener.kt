package io.oneprofile.core.notification

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class NotificationEventListener(val webHandler: NotificationWebHandler,
                                private final val env: Environment
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener(condition = "#event.isEmail()")
    fun handleEmailContactNotificationEvent(event: NotificationEvent) {
        try {
            webHandler.sendEmail(event.emailNotification!!)
        } catch (ex: Throwable) {
            throw EmailNotificationEventException("Failed to send an email to ${event.emailNotification?.emailTo} from the notification event ${event.emailNotification}", ex)
        }
        logger.info("an email si successfully sent to ${event.emailNotification?.emailTo} to from the notification event ${event.emailNotification} ")
    }
}

class EmailNotificationEventException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
