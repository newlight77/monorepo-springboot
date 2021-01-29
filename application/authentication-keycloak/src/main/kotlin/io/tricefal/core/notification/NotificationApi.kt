package io.tricefal.core.notification

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("notification")
class NotificationApi(val notificationWebHandler: NotificationWebHandler) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("contact")
    @ResponseStatus(HttpStatus.OK)
    fun contact(request: HttpServletRequest, @RequestBody notification: EmailContactNotificationModel) {
        logger.info("contact from ${request.remoteAddr} with notification=$notification")
        notification.origin = request.remoteAddr
        notificationWebHandler.sendContactEmail(notification)
    }

    @PostMapping("feedback")
    @ResponseStatus(HttpStatus.OK)
    fun feedback(request: HttpServletRequest, @RequestBody notification: EmailFeedbackNotificationModel) {
        logger.info("feedback from ${request.remoteAddr} with notification=$notification")
        notification.origin = request.remoteAddr
        notificationWebHandler.sendFeedbackEmail(notification)
    }

}