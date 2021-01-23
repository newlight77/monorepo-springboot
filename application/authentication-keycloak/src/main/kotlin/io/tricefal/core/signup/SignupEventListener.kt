package io.tricefal.core.signup

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class SignupEventListener(val signupService: ISignupService,
                          private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener
    fun handleResumeUploadedEvent(event: CompanyCompletionEvent): SignupStateDomain = try {
        logger.info("EventHandler picked up a company completion event with ${event.username}")
        this.signupService.companyCompleted(event.username)
    } catch(ex: Throwable) {
        logger.error("Failed to update the signup on company completion for username ${event.username}")
        throw CompanyCompletionException("Failed to update the signup on company completion for username ${event.username}", ex)
    }

    class CompanyCompletionException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
}