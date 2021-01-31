package io.tricefal.core.signup

import io.tricefal.core.profile.ProfileUploadException
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class SignupEventListener(val signupService: ISignupService,
                          private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener
    fun handCompanyCompletedEvent(event: CompanyCompletionEvent): SignupStateDomain = try {
        logger.info("EventHandler picked up a company completion event with ${event.username}")
        this.signupService.companyCompleted(event.username)
    } catch(ex: Throwable) {
        logger.error("Failed to update the signup on company completion for username ${event.username}")
        throw CompanyCompletionException("Failed to update the signup on company completion for username ${event.username}", ex)
    }

    @EventListener
    fun handleResumeUploadedEvent(event: ProfileResumeUploadedEvent) {
        try {
            this.signupService.profileResumeUploaded(event.username, event.filename)
        } catch(ex: Exception) {
            logger.error("Failed to update the profile resume for username ${event.username}")
            throw ProfileUploadException("Failed to update the profile resume for username ${event.username}", ex)
        }
        logger.info("EventHandler picked up a resume uploaded event with $event")
    }

    @EventListener
    fun handleResumeLinkedinUploadedEvent(event: ProfileResumeLinkedinUploadedEvent) {
        try {
            this.signupService.profileResumeLinkedinUploaded(event.username, event.filename)
        } catch(ex: Exception) {
            logger.error("Failed to update the profile resume linkedin for username ${event.username}")
            throw ProfileUploadException("Failed to update the profile resume linkedin for username ${event.username}", ex)
        }
        logger.info("EventHandler picked up resume linkedin uploaded event $event")
    }

    class CompanyCompletionException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
}