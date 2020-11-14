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
            val domain = FreelanceDomain.Builder(event.username)
                    .state(FreelanceStateDomain.Builder(event.username).build()).build()
            freelanceService.create(domain)
        } catch (ex: Exception) {
            throw FreelanceWebHandler.FreelanceCreationException("Failed to create a freelance profile with username ${event.username}")
        }
        logger.info("FreelanceEventListener picked up a FreelanceStatusUpdatedEvent with ${event.username}")
    }
}