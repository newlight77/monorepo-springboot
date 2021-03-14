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

    fun publishNewSignupEvent(signup: SignupDomain) {
        try {
            applicationEventPublisher.publishEvent(
                NewSignupEvent(signup.username,
                    signup.status.toString(),
                    signup.firstname,
                    signup.lastname,
                    signup.phoneNumber,
                )
            )
            logger.info("A NewSignupEvent has been published user ${signup.username}")
        } catch (ex: Exception) {
            logger.error("Failed to publish a NewSignupEvent for user ${signup.username}", ex)
            throw SignupStatusPublicationException("Failed to publish a NewSignupEvent for user ${signup.username}", ex)
        }
    }

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
            logger.error("Failed to publish a SignupStatusUpdatedEvent for user ${signup.username}", ex)
            throw SignupStatusPublicationException("Failed to publish a SignupStatusUpdatedEvent for user ${signup.username}", ex)
        }
    }

    fun publishStateUpdatedEvent(username: String, state: SignupState) {
        try {
            applicationEventPublisher.publishEvent(
                SignupStateUpdatedEvent(username, state.toString())
            )
            logger.info("A SignupStateUpdatedEvent has been published user $username")
        } catch (ex: Exception) {
            logger.error("Failed to publish a SignupStateUpdatedEvent for user $username", ex)
            throw SignupStatePublicationException("Failed to publish a SignupStateUpdatedEvent for user $username", ex)
        }
    }

    fun publishResumeUploadedEvent(username: String, filename: String) {
        try {
            applicationEventPublisher.publishEvent(
                SignupResumeUploadedEvent(username, filename)
            )
            logger.info("A ResumeUploadedEvent has been published: $filename for user $username")
        } catch (ex: Exception) {
            logger.error("Failed to publish a ResumeUploadedEvent for user $username", ex)
            throw ProfileResumeUploadedPublicationException("Failed to publish a ResumeUploadedEvent for user $username", ex)
        }
    }

    fun publishResumeLinkedinUploadedEvent(username: String, filename: String) {
        try {
            applicationEventPublisher.publishEvent(
                SignupResumeLinkedinUploadedEvent(username, filename)
            )
            logger.info("A ResumeLinkedinUploadedEvent has been published: $filename for user $username")
        } catch (ex: Exception) {
            logger.error("Failed to publish a ResumeLinkedinUploadedEvent for user $username", ex)
            throw ProfileResumeLinkedinUploadedPublicationException("Failed to publish a ResumeLinkedinUploadedEvent for user $username", ex)
        }
    }

    fun publishCguAcceptedEvent(username: String, cguAcceptedVersion: String) {
        try {
            applicationEventPublisher.publishEvent(
                CguAcceptedEvent(username, cguAcceptedVersion)
            )
            logger.info("A CguAcceptedEvent has been published for user $username with version $cguAcceptedVersion")
        } catch (ex: Exception) {
            logger.error("Failed to publish a CguAcceptedEvent for user $username with version $cguAcceptedVersion", ex)
            throw CguAcceptedPublicationException("Failed to publish a CguAcceptedEvent for user $username with version $cguAcceptedVersion", ex)
        }
    }

    fun publishEmailNotification(notification: EmailNotificationDomain) {
        try {
            applicationEventPublisher.publishEvent(
                NotificationEvent(notification)
            )
            logger.info("A EmailNotificationEvent has been published to ${notification.emailTo}")
        } catch (ex: Exception) {
            logger.error("Failed to publish a EmailNotificationEvent to ${notification.emailTo}", ex)
            throw EmailNotifiicationPublicationException("Failed to publish a EmailNotificationEvent to ${notification.emailTo}", ex)
        }
    }

    fun publishCommentAddedEvent(targetUsername: String, comment: CommentDomain) {
        applicationEventPublisher.publishEvent(
            SignupCommentAddedEvent(targetUsername, comment)
        )
    }

}

class SignupStatusPublicationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class SignupStatePublicationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class ProfileResumeUploadedPublicationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class ProfileResumeLinkedinUploadedPublicationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class CguAcceptedPublicationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}

class EmailNotifiicationPublicationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}

