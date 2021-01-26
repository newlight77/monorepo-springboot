package io.tricefal.core.freelance

import io.tricefal.core.signup.SignupStatusUpdatedEvent
import io.tricefal.core.signup.Status
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class FreelanceEventListener(val freelanceService: IFreelanceService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener(condition = "#event.isFreelance()")
    fun handleStatusUpdatedEvent(event: SignupStatusUpdatedEvent) {
        try {
            val freelance: FreelanceDomain = FreelanceDomain
                .Builder(event.signup.username)
                .withMission(Status.FREELANCE_WITH_MISSION === event.signup.status)
                .contact(ContactDomain.Builder()
                    .email(event.signup.username)
                    .firstName(event.signup.firstname)
                    .lastName(event.signup.lastname)
                    .phone(event.signup.phoneNumber)
                    .lastDate(Instant.now())
                    .build())
                .build()
            freelanceService.signupStatusUpdated(freelance)
        } catch (ex: Throwable) {
            throw SignupStatusUpdatedException("Failed to create a freelance profile upon status updated for username ${event.signup.username}")
        }
        logger.info("FreelanceEventListener picked up a FreelanceStatusUpdatedEvent with ${event.signup.username}")
    }
}

class SignupStatusUpdatedException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}