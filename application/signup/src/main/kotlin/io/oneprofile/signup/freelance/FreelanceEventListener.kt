package io.oneprofile.signup.freelance

import io.oneprofile.signup.company.ContactDomain
import io.oneprofile.signup.signup.SignupStatusUpdatedEvent
import io.oneprofile.signup.signup.Status
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class FreelanceEventListener(val webHandler: FreelanceWebHandler) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener(condition = "#event.isFreelance()")
    fun handleStatusUpdatedEvent(event: SignupStatusUpdatedEvent) {
        try {
            val freelance: FreelanceDomain = FreelanceDomain
                .Builder(event.username)
                .withMission(Status.FREELANCE_WITH_MISSION.toString() === event.status)
                .contact(ContactDomain.Builder()
                    .email(event.username)
                    .firstName(event.firstname)
                    .lastName(event.lastname)
                    .phone(event.phoneNumber)
                    .lastDate(Instant.now())
                    .build())
                .build()
            webHandler.initFreelance(freelance)
        } catch (ex: Throwable) {
            throw SignupStatusUpdatedException("Failed to create a freelance profile upon status updated for username ${event.username}")
        }
        logger.info("FreelanceEventListener picked up a FreelanceStatusUpdatedEvent with ${event.username}")
    }
}

class SignupStatusUpdatedException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}