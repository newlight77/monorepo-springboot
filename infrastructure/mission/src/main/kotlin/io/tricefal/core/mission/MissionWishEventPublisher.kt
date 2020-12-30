package io.tricefal.core.mission

import io.tricefal.core.metafile.MetafileDomain
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class MissionWishEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishMissionResumeUploadedEvent(fileDomain: MetafileDomain) {
        try {
            applicationEventPublisher.publishEvent(
                MissionResumeUploadedEvent(fileDomain)
            )
            logger.info("A MissionResumeUploadedEvent has been published: ${fileDomain.filename} for user ${fileDomain.username}")
        } catch (ex: Exception) {
            logger.error("Failed to publish a MissionResumeUploadedEvent for user ${fileDomain.username}")
            throw MissionResumeUpdatedPublicationException("Failed to publish a MissionResumeUploadedEvent for user ${fileDomain.username}")
        }
    }
}

class MissionResumeUpdatedPublicationException(private val msg: String) : Throwable(msg) {}
