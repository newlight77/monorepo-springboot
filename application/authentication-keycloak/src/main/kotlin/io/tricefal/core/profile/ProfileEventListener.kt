package io.tricefal.core.profile

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class ProfileEventListener(val profileService: IProfileService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener(condition = "#event.isPortrait()")
    fun handlePortraitUploadedEvent(event: PortraitUploadedEvent): ProfileModel {
        val result = try {
            this.profileService.updateProfileOnPortraitUploaded(event.username, event.metafile.filename)
        } catch(ex: Exception) {
            logger.error("Failed to update the profile portrait for username ${event.username}")
            throw ProfileUploadException("Failed to update the profile portrait for username ${event.username}")
        }
        logger.info("EventHandler picked up portraint upload event with ${event.metafile}")
        return toModel(result)
    }

    @EventListener(condition = "#event.isResume()")
    fun handleResumeUploadedEvent(event: ResumeUploadedEvent): ProfileModel {
        val result = try {
            this.profileService.updateProfileOnResumeUploaded(event.username, event.metafile.filename)
        } catch(ex: Exception) {
            logger.error("Failed to update the profile resume for username ${event.username}")
            throw ProfileUploadException("Failed to update the profile resume for username ${event.username}")
        }
        logger.info("EventHandler picked up a resume uploaded event with ${event.metafile}")
        return toModel(result)
    }

    @EventListener(condition = "#event.isResumeLinkedin()")
    fun handleResumeLinkedinUploadedEvent(event: ResumeLinkedinUploadedEvent): ProfileModel {
        val result = try {
            this.profileService.updateProfileOnResumeLinkedinUploaded(event.username, event.metafile.filename)
        } catch(ex: Exception) {
            logger.error("Failed to update the profile ref for username ${event.username}")
            throw ProfileUploadException("Failed to update the profile ref for username ${event.username}")
        }
        logger.info("EventHandler picked up ref uploaded event ${event.metafile}")
        return toModel(result)
    }

}