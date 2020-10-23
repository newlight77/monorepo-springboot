package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileDomain
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ProfileEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishPortraitUploadedEvent(fileDomain: MetafileDomain) {
        applicationEventPublisher.publishEvent(
                PortraitUploadedEvent(fileDomain)
        )
        logger.info("A PortraitUploadedEvent has been published: ${fileDomain.filename} for user ${fileDomain.username}")
    }

    fun publishResumeUploadedEvent(fileDomain: MetafileDomain) {
        applicationEventPublisher.publishEvent(
                ResumeUploadedEvent(fileDomain)
        )
        logger.info("A PortraitUploadedEvent has been published: ${fileDomain.filename} for user ${fileDomain.username}")
    }

    fun publishRefUploadedEvent(fileDomain: MetafileDomain) {
        applicationEventPublisher.publishEvent(
                RefUploadedEvent(fileDomain)
        )
        logger.info("A PortraitUploadedEvent has been published: ${fileDomain.filename} for user ${fileDomain.username}")
    }
}

