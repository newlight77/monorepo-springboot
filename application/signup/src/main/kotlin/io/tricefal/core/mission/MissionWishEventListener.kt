package io.tricefal.core.mission

import io.tricefal.core.signup.SignupResumeUploadedEvent
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class MissionWishEventListener(val webHandler: MissionWishWebHandler) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener
    fun handleResumeUploadedEvent(event: SignupResumeUploadedEvent) {
        try {
            this.webHandler.updateOnResumeUploaded(event.username, event.filename)
        } catch(ex: Throwable) {
            logger.error("Failed to update the mission wish on resume uploaded for username ${event.username}", ex)
            throw MissionWishCreationException("Failed to update the mission wish on resume uploaded for username ${event.username}", ex)
        }
        logger.info("EventHandler picked up a resume uploaded event with ${event.filename}")
    }

    class MissionWishCreationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
}