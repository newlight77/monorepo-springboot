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
            this.profileService.updateProfileOnPortraitUploaded(event.username, event.portrait.filename)
        } catch(ex: Exception) {
            logger.error("Failed to update the profile portrait for username ${event.username}")
            throw ProfileUploadException("Failed to update the profile portrait for username ${event.username}")
        }
        logger.info("EventHandler picked up portraint upload event with ${event.portrait}")
        return toModel(result)
    }

    @EventListener(condition = "#event.isResume()")
    fun handleResumeUploadedEvent(event: ResumeUploadedEvent): ProfileModel {
        val result = try {
            this.profileService.updateProfileOnResumeUploaded(event.username, event.resume.filename)
        } catch(ex: Exception) {
            logger.error("Failed to update the profile resume for username ${event.username}")
            throw ProfileUploadException("Failed to update the profile resume for username ${event.username}")
        }
        logger.info("EventHandler picked up a resume uploaded event with ${event.resume}")
        return toModel(result)
    }

    @EventListener(condition = "#event.isRef()")
    fun handleRefUploadedEvent(event: RefUploadedEvent): ProfileModel {
        val result = try {
            this.profileService.updateProfileOnRefUploaded(event.username, event.ref.filename)
        } catch(ex: Exception) {
            logger.error("Failed to update the profile ref for username ${event.username}")
            throw ProfileUploadException("Failed to update the profile ref for username ${event.username}")
        }
        logger.info("EventHandler picked up ref uploaded event ${event.ref}")
        return toModel(result)
    }

}