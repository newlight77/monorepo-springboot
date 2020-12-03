package io.tricefal.core.signup

import io.tricefal.core.cgu.CguAcceptedEvent
import io.tricefal.core.metafile.MetafileDomain
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class SignupEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishStatusUpdatedEvent(username: String, status: String) {
        try {
            applicationEventPublisher.publishEvent(
                    SignupStatusUpdatedEvent(username, status)
            )
            logger.info("A FreelanceStatusUpdatedEvent has been published user $username")
        } catch (ex: Exception) {
            logger.error("Failed to publish a SignupStatusUpdatedEvent for user $username")
            throw FreelanceStatusPublicationException("Failed to publish a SignupStatusUpdatedEvent for user $username")
        }
    }

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

    fun publishCguAcceptedEvent(username: String, cguAcceptedVersion: String) {
        try {
            applicationEventPublisher.publishEvent(
                    CguAcceptedEvent(username, cguAcceptedVersion)
            )
            logger.info("A CguAcceptedEvent has been published for user $username with version $cguAcceptedVersion")
        } catch (ex: Exception) {
            logger.error("Failed to publish a CguAcceptedEvent for user $username with version $cguAcceptedVersion")
            throw CguAcceptedPublicationException("Failed to publish a CguAcceptedEvent for user $username with version $cguAcceptedVersion")
        }
    }

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

class FreelanceStatusPublicationException(private val msg: String) : Throwable(msg) {}
class ProfilePortraitUploadedPublicationException(private val msg: String) : Throwable(msg) {}
class ProfileResumeUploadedPublicationException(private val msg: String) : Throwable(msg) {}
class ProfileResumeLinkedinUploadedPublicationException(private val msg: String) : Throwable(msg) {}
class CguAcceptedPublicationException(private val msg: String) : Throwable(msg) {}
class MissionResumeUpdatedPublicationException(private val msg: String) : Throwable(msg) {}


