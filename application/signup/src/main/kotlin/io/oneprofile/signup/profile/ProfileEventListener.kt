package io.oneprofile.signup.profile

import io.oneprofile.signup.signup.*
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ProfileEventListener(val webHandler: ProfileWebHandler) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener
    fun handleNewSignupEvent(event: NewSignupEvent) {
        try {
            val profile = ProfileDomain.Builder(event.username)
                .firstname(event.firstname)
                .lastname(event.lastname)
                .phoneNumber(event.phoneNumber)
                .status(Status.valueOf(event.status))
                .build()
            this.webHandler.initProfile(event.username, profile)
        } catch (ex: Throwable) {
            throw ProfileStatusUpdateException("Failed to create a new profile with username ${event.username}", ex)
        }
        logger.info("ProfileEventListener picked up a NewSignupEvent with ${event.username}")
    }

    @EventListener
    fun handleStatusUpdatedEvent(event: SignupStatusUpdatedEvent) {
        try {
            this.webHandler.updateStatus(event.username, Status.valueOf(event.status))
        } catch (ex: Throwable) {
            throw ProfileStatusUpdateException("Failed to update the status of the profile with username ${event.username}", ex)
        }
        logger.info("ProfileEventListener picked up a SignupStatusUpdatedEvent with ${event.username}")
    }

    @EventListener
    fun handleSignupStateUpdatedEvent(event: SignupStateUpdatedEvent) {
        try {
            this.webHandler.updateState(event.username, event.state)
        } catch (ex: Throwable) {
            throw ProfileStateUpdateException("Failed to update the signup state of the profile with username ${event.username}", ex)
        }
        logger.info("ProfileEventListener picked up a SignupStateUpdatedEvent with ${event.username}")
    }

    @EventListener
    fun handleResumeUploadedEvent(event: SignupResumeUploadedEvent) {
        try {
            this.webHandler.updateProfileOnResumeUploaded(event.username, event.filename)
        } catch(ex: Exception) {
            logger.error("Failed to update the profile resume for username ${event.username}", ex)
            throw ProfileUploadException("Failed to update the profile resume for username ${event.username}", ex)
        }
        logger.info("EventHandler picked up a resume uploaded event with ${event.filename}")
    }

    @EventListener
    fun handleResumeLinkedinUploadedEvent(event: SignupResumeLinkedinUploadedEvent) {
        try {
            this.webHandler.updateProfileOnResumeLinkedinUploaded(event.username, event.filename)
        } catch(ex: Exception) {
            logger.error("Failed to update the profile resume linkedin for username ${event.username}", ex)
            throw ProfileUploadException("Failed to update the profile resume linkedin for username ${event.username}", ex)
        }
        logger.info("EventHandler picked up resume linkedin uploaded event ${event.filename}")
    }

}

class ProfileStatusUpdateException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}

class ProfileStateUpdateException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
