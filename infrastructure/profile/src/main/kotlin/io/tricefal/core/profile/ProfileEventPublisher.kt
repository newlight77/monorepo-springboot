package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileDomain
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ProfileEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishPortraitUploadedEvent(fileDomain: MetafileDomain) {
        try {
            applicationEventPublisher.publishEvent(
                    PortraitUploadedEvent(fileDomain)
            )
            logger.info("A PortraitUploadedEvent has been published: ${fileDomain.filename} for user ${fileDomain.username}")
        } catch (ex: Exception) {
            logger.error("Failed to publish a PortraitUploadedEvent for user ${fileDomain.username}")
            throw ProfilePortraitUploadedPublicationException("Failed to publish a PortraitUploadedEvent for user ${fileDomain.username}")
        }
    }

    fun publishResumeUploadedEvent(fileDomain: MetafileDomain) {
        try {
            applicationEventPublisher.publishEvent(
                    ResumeUploadedEvent(fileDomain)
            )
            logger.info("A ResumeUploadedEvent has been published: ${fileDomain.filename} for user ${fileDomain.username}")
        } catch (ex: Exception) {
            logger.error("Failed to publish a ResumeUploadedEvent for user ${fileDomain.username}")
            throw ProfileResumeUploadedPublicationException("Failed to publish a ResumeUploadedEvent for user ${fileDomain.username}")
        }
    }

    fun publishResumeLinkedinUploadedEvent(fileDomain: MetafileDomain) {
        try {
            applicationEventPublisher.publishEvent(
                    ResumeLinkedinUploadedEvent(fileDomain)
            )
            logger.info("A ResumeLinkedinUploadedEvent has been published: ${fileDomain.filename} for user ${fileDomain.username}")
        } catch (ex: Exception) {
            logger.error("Failed to publish a ResumeLinkedinUploadedEvent for user ${fileDomain.username}")
            throw ProfileResumeLinkedinUploadedPublicationException("Failed to publish a ResumeLinkedinUploadedEvent for user ${fileDomain.username}")
        }
    }

    class ProfilePortraitUploadedPublicationException(private val msg: String) : Throwable(msg) {}
    class ProfileResumeUploadedPublicationException(private val msg: String) : Throwable(msg) {}
    class ProfileResumeLinkedinUploadedPublicationException(private val msg: String) : Throwable(msg) {}
}

