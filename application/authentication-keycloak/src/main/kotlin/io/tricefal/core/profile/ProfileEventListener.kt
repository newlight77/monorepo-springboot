package io.tricefal.core.profile

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ProfileEventListener(val profileService: IProfileService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @EventListener(condition = "#event.isPortrait()")
    fun handlePortraitUploadedEvent(event: PortraitUploadedEvent) {
        this.profileService.updateProfileOnPortraitUploaded(event.username, event.portrait.filename)
        logger.info("EventHandler picked up portraint upload event with ${event.portrait}")
    }

    @EventListener(condition = "#event.isResume()")
    fun handleResumeUploadedEvent(event: ResumeUploadedEvent) {
        this.profileService.updateProfileOnResumeUploaded(event.username, event.resume.filename)
        logger.info("EventHandler picked up a resume uploaded event with ${event.resume}")
    }

    @EventListener(condition = "#event.isRef()")
    fun handleRefUploadedEvent(event: RefUploadedEvent) {
        this.profileService.updateProfileOnRefUploaded(event.username, event.ref.filename)
        logger.info("EventHandler picked up ref uploaded event ${event.ref}")
    }

}