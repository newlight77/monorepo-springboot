package io.oneprofile.signup.mission

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class MissionWishEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishMissionResumeUploadedEvent(username: String, filename: String) {
        try {
            applicationEventPublisher.publishEvent(
                MissionResumeUploadedEvent(username, filename)
            )
            logger.info("A MissionResumeUploadedEvent has been published: $filename for user $username")
        } catch (ex: Exception) {
            logger.error("Failed to publish a MissionResumeUploadedEvent for user $username", ex)
            throw MissionResumeUpdatedPublicationException("Failed to publish a MissionResumeUploadedEvent for user $username", ex)
        }
    }
}

class MissionResumeUpdatedPublicationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
