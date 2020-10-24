package io.tricefal.core.signup

import io.tricefal.core.email.EmailMessage
import io.tricefal.core.email.EmailService
import io.tricefal.core.email.EmailTemplate
import io.tricefal.core.login.SignupJpaRepository
import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.okta.IamRegisterService
import io.tricefal.core.profile.ProfileEventPublisher
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
                    val profileEventListener: ProfileEventPublisher
) : ISignupAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun save(signup: SignupDomain): SignupDomain {
        repository.findByUsername(signup.username).ifPresent {
            logger.error("a signup with username ${signup.username} is already taken")
            throw DuplicateUsernameException("a signup with username ${signup.username} is already taken")
        }
        val signupEntity = repository.save(toEntity(signup))
        return fromEntity(signupEntity)
    }

    override fun delete(username: String) {
        repository.findByUsername(username).ifPresentOrElse (
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
        return repository.findByUsername(username).map {
            fromEntity(it)
        }
    }

    override fun update(signup: SignupDomain): SignupDomain {
        val mewEntity = toEntity(signup)
        repository.findByUsername(signup.username).ifPresentOrElse(
                { mewEntity.id = it.id },
                {
                    logger.error("unable to find a registration with username ${signup.username}")
                    throw SignupNotFoundException("unable to find a registration with username ${signup.username}")
                }
        )
        val signupEntity = repository.save(mewEntity)
        return fromEntity(signupEntity)
    }

    override fun register(signup: SignupDomain): Boolean {
        return try {
            registrationService.register(signup)
        } catch (ex: Exception) {
            logger.error("Failed to register a user on IAM server for username ${signup.username}")
            throw RegistrationException("Failed to register a user on IAM server for username ${signup.username}")
        }
    }

    override fun sendSms(notification: SignupNotificationDomain): Boolean {
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

    override fun sendEmail(notification: SignupNotificationDomain): Boolean {
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
        try {
            keycloakRegisterService.addRole(signup.username, statusToRole[signup.status]!!)
        } catch (ex: Exception) {
            logger.error("Failed to assign the role ${statusToRole[signup.status]} to user ${signup.username}")
            throw RoleAssignationException("Failed to assign the role ${statusToRole[signup.status]} to user ${signup.username}")
        }
        logger.info("User status updated to ${signup.status} and role ${statusToRole[signup.status]} has been assigned to user ${signup.username}")
        return update(signup)
    }

    override fun portraitUploaded(fileDomain: MetafileDomain) {
        this.profileEventListener.publishPortraitUploadedEvent(fileDomain)
    }

    override fun resumeUploaded(fileDomain: MetafileDomain) {
        this.profileEventListener.publishResumeUploadedEvent(fileDomain)
    }

    override fun refUploaded(fileDomain: MetafileDomain) {
        this.profileEventListener.publishRefUploadedEvent(fileDomain)
    }

    val statusToRole = mapOf(
            Status.FREELANCE to AccessRight.AC_FREELANCE_READ,
            Status.EMPLOYEE to AccessRight.AC_COLLABORATOR_READ,
            Status.CLIENT to AccessRight.AC_CLIENT_READ
    )

    class DuplicateUsernameException(private val msg: String) : Throwable(msg) {}
    class SignupNotFoundException(private val msg: String) : Throwable(msg) {}
    class SignupEmailNotificationException(private val msg: String) : Throwable(msg) {}
    class SignupSmsNotificationException(private val msg: String) : Throwable(msg) {}
    class RegistrationException(private val msg: String) : Throwable(msg) {}
    class RoleAssignationException(private val msg: String) : Throwable(msg) {}
}





