package io.tricefal.core.signup

import io.tricefal.core.cgu.CguAcceptedEvent
import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.NotificationEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class SignupEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun publishStatusUpdatedEvent(signup: SignupDomain) {
        try {
            applicationEventPublisher.publishEvent(
                SignupStatusUpdatedEvent(signup.username,
                    signup.status.toString(),
                    signup.firstname,
                    signup.lastname,
                    signup.phoneNumber,
                )
            )
            logger.info("A SignupStatusUpdatedEvent has been published user ${signup.username}")
        } catch (ex: Exception) {
            logger.error("Failed to publish a SignupStatusUpdatedEvent for user ${signup.username}")
            throw SignupStatusPublicationException("Failed to publish a SignupStatusUpdatedEvent for user ${signup.username}")
        }
    }

    fun publishStateUpdatedEvent(username: String, state: SignupState) {
        try {
            applicationEventPublisher.publishEvent(
                SignupStateUpdatedEvent(username, state.toString())
            )
            logger.info("A SignupStateUpdatedEvent has been published user $username")
        } catch (ex: Exception) {
            logger.error("Failed to publish a SignupStateUpdatedEvent for user $username")
            throw SignupStatePublicationException("Failed to publish a SignupStateUpdatedEvent for user $username")
        }
    }

    fun publishResumeUploadedEvent(username: String, filename: String) {
        try {
            applicationEventPublisher.publishEvent(
                SignupResumeUploadedEvent(username, filename)
            )
            logger.info("A ResumeUploadedEvent has been published: $filename for user $username")
        } catch (ex: Exception) {
            logger.error("Failed to publish a ResumeUploadedEvent for user $username")
            throw ProfileResumeUploadedPublicationException("Failed to publish a ResumeUploadedEvent for user $username")
        }
    }

    fun publishResumeLinkedinUploadedEvent(username: String, filename: String) {
        try {
            applicationEventPublisher.publishEvent(
                SignupResumeLinkedinUploadedEvent(username, filename)
            )
            logger.info("A ResumeLinkedinUploadedEvent has been published: $filename for user $username")
        } catch (ex: Exception) {
            logger.error("Failed to publish a ResumeLinkedinUploadedEvent for user $username")
            throw ProfileResumeLinkedinUploadedPublicationException("Failed to publish a ResumeLinkedinUploadedEvent for user $username")
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

    fun publishEmailNotification(notification: EmailNotificationDomain) {
        try {
            applicationEventPublisher.publishEvent(
                NotificationEvent(notification)
            )
            logger.info("A EmailNotificationEvent has been published to ${notification.emailTo}")
        } catch (ex: Exception) {
            logger.error("Failed to publish a EmailNotificationEvent to ${notification.emailTo}")
            throw EmailNotifiicationPublicationException("Failed to publish a EmailNotificationEvent to ${notification.emailTo}")
        }
    }

    fun publishCommentAddedEvent(targetUsername: String, comment: CommentDomain) {
        applicationEventPublisher.publishEvent(
            SignupCommentAddedEvent(targetUsername, comment)
        )
    }

}

class SignupStatusPublicationException(private val msg: String) : Throwable(msg) {}
class SignupStatePublicationException(private val msg: String) : Throwable(msg) {}
class ProfileResumeUploadedPublicationException(private val msg: String) : Throwable(msg) {}
class ProfileResumeLinkedinUploadedPublicationException(private val msg: String) : Throwable(msg) {}
class CguAcceptedPublicationException(private val msg: String) : Throwable(msg) {}

class EmailNotifiicationPublicationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}

