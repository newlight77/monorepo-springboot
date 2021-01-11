package io.tricefal.core.freelance

import io.tricefal.core.signup.SignupStatusUpdatedEvent
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class FreelanceEventListener(val freelanceService: IFreelanceService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener(condition = "#event.isFreelance()")
    fun handleStatusUpdatedEvent(event: SignupStatusUpdatedEvent) {
        try {
            freelanceService.signupStatusUpdated(event.username, event.status)
        } catch (ex: Throwable) {
            throw SignupStatusUpdatedException("Failed to create a freelance profile upon status updated for username ${event.username}")
        }
        logger.info("FreelanceEventListener picked up a FreelanceStatusUpdatedEvent with ${event.username}")
    }
}

class SignupStatusUpdatedException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}