package io.tricefal.core.signup

import io.tricefal.core.login.SignupJpaRepository
import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.NotificationAdapter
import io.tricefal.core.notification.SmsNotificationDomain
import io.tricefal.core.okta.IamRegisterService
import io.tricefal.core.right.AccessRight
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class SignupRepositoryAdapter(private var repository: SignupJpaRepository,
                              val registrationService: IamRegisterService,
                              val notificationAdapter: NotificationAdapter,
                              val keycloakRegisterService: IamRegisterService,
                              val signupEventPublisher: SignupEventPublisher
) : SignupDataAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun save(signup: SignupDomain): SignupDomain {
        repository.findByUsername(signup.username).stream().findFirst().ifPresent {
            logger.error("a signup with username ${signup.username} is already taken")
            throw SignupUsernameUniquenessException("a signup with username ${signup.username} is already taken")
        }
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

    override fun update(signup: SignupDomain): Optional<SignupDomain> {
        val signupEntity = repository.findByUsername(signup.username).stream().findFirst()
        var updated = Optional.empty<SignupDomain>()
        signupEntity.ifPresentOrElse(
                {
                    val mewEntity = toEntity(signup)
                    mewEntity.id = it.id
                    repository.save(mewEntity)
                    updated =  Optional.of(fromEntity(mewEntity))
                },
                {
                    logger.error("unable to find a registration with username ${signup.username}")
                    throw SignupNotFoundException("unable to find a registration with username ${signup.username}")
                }
        )
        return updated
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
        return notificationAdapter.sendSms(notification)
    }

    override fun sendEmail(signupNotification: SignupEmailNotificationDomain): Boolean {
        val notification: EmailNotificationDomain = toEmaail(signupNotification)
        return notificationAdapter.sendEmail(notification)
    }

    private fun toEmaail(signupNotification: SignupEmailNotificationDomain): EmailNotificationDomain {
        return EmailNotificationDomain.Builder(signupNotification.username)
            .emailFrom(signupNotification.emailFrom)
            .emailTo(signupNotification.emailTo)
            .emailSubject(signupNotification.emailSubject)
            .emailContent(signupNotification.emailContent)
            .emailGreeting(signupNotification.emailGreeting).build()
    }

    override fun updateStatus(signup: SignupDomain): Optional<SignupDomain>  {
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
    class SignupRegistrationException(private val msg: String) : Throwable(msg) {}
    class SignupRoleAssignationException(private val msg: String) : Throwable(msg) {}
    class SignupIamAccountDeletionException(private val msg: String) : Throwable(msg) {}
}





