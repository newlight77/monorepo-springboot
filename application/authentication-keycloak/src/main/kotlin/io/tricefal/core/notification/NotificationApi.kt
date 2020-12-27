package io.tricefal.core.notification

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("notification")
class NotificationApi(val notificationWebHandler: NotificationWebHandler) {

    @RolesAllowed("ROLE_user-role")
    @GetMapping("contact")
    @ResponseStatus(HttpStatus.OK)
    fun contact(@RequestBody notification: EmailContactNotificationModel): Boolean {
        return notificationWebHandler.sendContactEmail(notification)
    }

    @RolesAllowed("ROLE_user-role")
    @GetMapping("feedback")
    @ResponseStatus(HttpStatus.OK)
    fun feedback(@RequestBody notification: EmailFeedbackNotificationModel): Boolean {
        return notificationWebHandler.sendFeedbackEmail(notification)
    }

}