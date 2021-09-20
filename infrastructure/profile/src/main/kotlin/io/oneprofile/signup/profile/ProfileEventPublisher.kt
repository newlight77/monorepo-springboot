package io.oneprofile.signup.profile

import io.oneprofile.signup.signup.ProfileResumeLinkedinUploadedEvent
import io.oneprofile.signup.signup.ProfileResumeUploadedEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ProfileEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishResumeUploadedEvent(username: String, filename: String) {
        try {
            applicationEventPublisher.publishEvent(
                ProfileResumeUploadedEvent(username, filename)
            )
            logger.info("A ResumeUploadedEvent has been published: $filename for user $username")
        } catch (ex: Exception) {
            logger.error("Failed to publish a ResumeUploadedEvent for user $username", ex)
            throw ProfileResumeUploadedPublicationException("Failed to publish a ResumeUploadedEvent for user $username", ex)
        }
    }

    fun publishResumeLinkedinUploadedEvent(username: String, filename: String) {
        try {
            applicationEventPublisher.publishEvent(
                ProfileResumeLinkedinUploadedEvent(username, filename)
            )
            logger.info("A ResumeLinkedinUploadedEvent has been published: $filename for user $username")
        } catch (ex: Exception) {
            logger.error("Failed to publish a ResumeLinkedinUploadedEvent for user $username", ex)
            throw ProfileResumeLinkedinUploadedPublicationException("Failed to publish a ResumeLinkedinUploadedEvent for user $username", ex)
        }
    }

}

class ProfileResumeUploadedPublicationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}

class ProfileResumeLinkedinUploadedPublicationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}

