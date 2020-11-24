package io.tricefal.core.mission

import io.tricefal.core.profile.ProfileUploadException
import io.tricefal.core.signup.ResumeUploadedEvent
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class MissionWishEventListener(val missionWishService: IMissionWishService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener(condition = "#event.isResume()")
    fun handleResumeUploadedEvent(event: ResumeUploadedEvent): MissionWishModel {
        val result = try {
            this.missionWishService.updateOnResumeUploaded(event.username, event.metafile.filename)
        } catch(ex: Throwable) {
            logger.error("Failed to update the mission wish on resume uploaded for username ${event.username}")
            throw ProfileUploadException("Failed to update the mission wish on resume uploaded for username ${event.username}")
        }
        logger.info("EventHandler picked up a resume uploaded event with ${event.metafile}")
        return toModel(result)
    }

    class MissionWishCreationException(private val msg: String) : Throwable(msg) {}
}