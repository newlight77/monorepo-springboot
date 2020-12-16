package io.tricefal.core.signup

import io.tricefal.core.email.EmailMessage
import io.tricefal.core.email.EmailService
import io.tricefal.core.email.EmailTemplate
import io.tricefal.core.login.SignupJpaRepository
import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.SmsNotificationDomain
import io.tricefal.core.okta.IamRegisterService
import io.tricefal.core.right.AccessRight
import io.tricefal.core.twilio.SmsMessage
import io.tricefal.core.twilio.SmsService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class SignupAdapter(private var repository: SignupJpaRepository,
                    val registrationService: IamRegisterService,
                    val mailService: EmailService,
                    val smsService: SmsService,
                    val keycloakRegisterService: IamRegisterService,
                    val signupEventPublisher: SignupEventPublisher
) : ISignupAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun save(signup: SignupDomain): SignupDomain {
        val signupEntity = repository.save(toEntity(signup))
        return fromEntity(signupEntity)
    }

    override fun delete(username: String) {
        repository.findByUsername(username).stream().findFirst().ifPresentOrElse (
            {
                registrationService.delete(username)
                repository.delete(it)
            },
            {
                logger.error("unable to delete a registration with username $username")
                throw SignupNotFoundException("unable to delete a registration with username $username")
            }
        )
    }

    override fun findAll(): List<SignupDomain> {
        return repository.findAll().map {
            fromEntity(it)
        }
    }

    override fun findByUsername(username: String): Optional<SignupDomain> {
        return repository.findByUsername(username).stream().findFirst().map {
            fromEntity(it)
        }
    }

    override fun update(signup: SignupDomain): SignupDomain {
        val mewEntity = toEntity(signup)
        val signupEntity = repository.findByUsername(signup.username).stream().findFirst()

        signupEntity.ifPresentOrElse(
                {
                    mewEntity.id = it.id
                    repository.flush()
                },
                {
                    logger.error("unable to find a registration with username ${signup.username}")
                    throw SignupNotFoundException("unable to find a registration with username ${signup.username}")
                }
        )

        return fromEntity(signupEntity.get())
    }

    override fun unregister(username: String): Boolean {
        return try {
            registrationService.delete(username)
        } catch (ex: Exception) {
            logger.error("failed to delete user on IAM server for username $username")
            throw SignupIamAccountDeletionException("failed to delete user from IAM server for username $username")
        }
    }

    override fun register(signup: SignupDomain): Boolean {
        return try {
            registrationService.register(signup)
        } catch (ex: Exception) {
            logger.error("Failed to register a user on IAM server for username ${signup.username}")
            throw SignupRegistrationException("Failed to register a user on IAM server for username ${signup.username}")
        }
    }

    override fun sendSms(notification: SmsNotificationDomain): Boolean {
        logger.info("Sending ans SMS")
        val result = try {
            val message = SmsMessage.Builder()
                    .from(notification.smsFrom!!)
                    .to(notification.smsTo!!)
                    .content(notification.smsContent!!)
                    .build()
            smsService.send(message).matches(Regex("^SM[a-z0-9]*"))
        } catch (ex: Exception) {
            logger.error("Failed to send a sms notification for user ${notification.smsTo}")
            throw SignupSmsNotificationException("Failed to send a sms notification for number ${notification.smsTo}")
        }
        logger.info("An SMS has been sent")
        return result
    }

    override fun sendEmail(notification: EmailNotificationDomain): Boolean {
        logger.info("Sending an email")
        try {
            val message = EmailMessage.Builder()
                    .from(notification.emailFrom!!)
                    .to(notification.emailTo!!)
                    .subject(notification.emailSubject!!)
                    .content(notification.emailContent!!)
                    .emailTemplate(EmailTemplate.SIGNUP)
                    .model(hashMapOf("greeting" to notification.emailGreeting!!, "content" to notification.emailContent!!))
                    .build()
            mailService.send(message)
        } catch (ex: Exception) {
            logger.error("Failed to send an email notification for user ${notification.emailTo}")
            throw SignupEmailNotificationException("Failed to send an email notification for user ${notification.emailTo}")
        }
        logger.info("An Email has been sent")
        return true
    }

    override fun updateStatus(signup: SignupDomain): SignupDomain {
        return update(signup)
    }

    override fun statusUpdated(signup: SignupDomain) {
        this.signupEventPublisher.publishStatusUpdatedEvent(signup.username, signup.status.toString())
    }

    override fun cguAccepted(username: String, cguAcceptedVersion: String) {
        this.signupEventPublisher.publishCguAcceptedEvent(username, cguAcceptedVersion)
    }

    override fun portraitUploaded(fileDomain: MetafileDomain) {
        this.signupEventPublisher.publishPortraitUploadedEvent(fileDomain)
    }

    override fun resumeUploaded(fileDomain: MetafileDomain) {
        this.signupEventPublisher.publishResumeUploadedEvent(fileDomain)
    }

    override fun resumeLinkedinUploaded(fileDomain: MetafileDomain) {
        this.signupEventPublisher.publishResumeLinkedinUploadedEvent(fileDomain)
    }

    override fun assignRole(username: String, accessRight: AccessRight) {
        try {
            keycloakRegisterService.addRole(username, accessRight)
        } catch (ex: Exception) {
            logger.error("Failed to assign the role $accessRight to user $username")
            throw SignupRoleAssignationException("Failed to assign the role $accessRight to user $username")
        }
    }

    class SignupUsernameUniquenessException(private val msg: String) : Throwable(msg) {}
    class SignupNotFoundException(private val msg: String) : Throwable(msg) {}
    class SignupEmailNotificationException(private val msg: String) : Throwable(msg) {}
    class SignupSmsNotificationException(private val msg: String) : Throwable(msg) {}
    class SignupRegistrationException(private val msg: String) : Throwable(msg) {}
    class SignupRoleAssignationException(private val msg: String) : Throwable(msg) {}
    class SignupIamAccountDeletionException(private val msg: String) : Throwable(msg) {}
}





