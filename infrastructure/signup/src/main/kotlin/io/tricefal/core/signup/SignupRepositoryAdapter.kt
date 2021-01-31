package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.NotificationAdapter
import io.tricefal.core.notification.SmsNotificationDomain
import io.tricefal.core.okta.IamRegisterService
import io.tricefal.core.right.AccessRight
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.Instant
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
        var entity = toEntity(signup)
        entity.lastDate = signup.lastDate ?: Instant.now()
        entity = repository.save(toEntity(signup))
        return fromEntity(entity)
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

    override fun softDelete(username: String) {
        repository.findByUsername(username).stream().findFirst().ifPresentOrElse (
            {
                this.signupEventPublisher.publishStateUpdatedEvent(username, SignupState.DELETED)
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
                    val newEntity = toEntity(signup)
                    newEntity.id = it.id
                    newEntity.lastDate = it.lastDate ?: Instant.now()
                    repository.save(newEntity)
                    updated =  Optional.of(fromEntity(newEntity))
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
            throw SignupIamAccountDeletionException("failed to delete user from IAM server for username $username", ex)
        }
    }

    override fun register(signup: SignupDomain): Boolean {
        return try {
            val result = registrationService.register(signup)
            this.signupEventPublisher.publishStateUpdatedEvent(signup.username, SignupState.REGISTERED)
            result
        } catch (ex: Exception) {
            logger.error("Failed to register a user on IAM server for username ${signup.username}")
            throw SignupRegistrationException("Failed to register a user on IAM server for username ${signup.username}", ex)
        }
    }

    override fun sendSms(username: String, notification: SmsNotificationDomain): Boolean {
        val result = notificationAdapter.sendSms(notification)
        this.signupEventPublisher.publishStateUpdatedEvent(username, SignupState.SMS_CODE_SENT)
        return result
    }

    override fun sendEmail(notification: EmailNotificationDomain): Boolean {
//        val result = notificationAdapter.sendEmail(notification)
//        this.signupEventPublisher.publishStateUpdatedEvent(username, SignupState.EMAIL_SENT.toString())
        this.signupEventPublisher.publishEmailNotification(notification)
        return true
    }

    override fun emailSent(signup: SignupDomain) {
        this.signupEventPublisher.publishStateUpdatedEvent(signup.username, SignupState.EMAIL_SENT)
    }

    override fun statusUpdated(signup: SignupDomain) {
        this.signupEventPublisher.publishStatusUpdatedEvent(signup)
        this.signupEventPublisher.publishStateUpdatedEvent(signup.username, SignupState.STATUS_SET)
    }

    override fun cguAccepted(username: String, cguAcceptedVersion: String) {
        this.signupEventPublisher.publishCguAcceptedEvent(username, cguAcceptedVersion)
        this.signupEventPublisher.publishStateUpdatedEvent(username, SignupState.CGU_ACCEPTED)
    }

    override fun resumeUploaded(fileDomain: MetafileDomain) {
        this.signupEventPublisher.publishResumeUploadedEvent(fileDomain)
        this.signupEventPublisher.publishStateUpdatedEvent(fileDomain.username, SignupState.RESUME_UPLOADED)
    }

    override fun resumeLinkedinUploaded(fileDomain: MetafileDomain) {
        this.signupEventPublisher.publishResumeLinkedinUploadedEvent(fileDomain)
        this.signupEventPublisher.publishStateUpdatedEvent(fileDomain.username, SignupState.RESUME_LINKEDIN_UPLOADED)
    }

    override fun validated(signup: SignupDomain) {
        this.signupEventPublisher.publishStateUpdatedEvent(signup.username, SignupState.VALIDATED)
    }

    override fun unvalidated(signup: SignupDomain) {
        this.signupEventPublisher.publishStateUpdatedEvent(signup.username, SignupState.UNVALIDATED)
    }

    override fun smsValidated(signup: SignupDomain) {
        this.signupEventPublisher.publishStateUpdatedEvent(signup.username, SignupState.SMS_CODE_VALIDATED)
    }

    override fun emailValidated(signup: SignupDomain) {
        this.signupEventPublisher.publishStateUpdatedEvent(signup.username, SignupState.EMAIL_VALIDATED)
    }

    override fun companyCompleted(signup: SignupDomain) {
        this.signupEventPublisher.publishStateUpdatedEvent(signup.username, SignupState.COMPLETED)
    }

    override fun assignRole(username: String, accessRight: AccessRight) {
        try {
            keycloakRegisterService.addRole(username, accessRight)
        } catch (ex: Exception) {
            logger.error("Failed to assign the role $accessRight to user $username")
            throw SignupRoleAssignationException("Failed to assign the role $accessRight to user $username", ex)
        }
    }

    class SignupUsernameUniquenessException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class SignupNotFoundException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class SignupRegistrationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class SignupRoleAssignationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class SignupIamAccountDeletionException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
}





