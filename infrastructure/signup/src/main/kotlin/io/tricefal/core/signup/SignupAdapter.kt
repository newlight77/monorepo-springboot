package io.tricefal.core.signup

import io.tricefal.core.email.EmailMessage
import io.tricefal.core.email.EmailService
import io.tricefal.core.email.EmailTemplate
import io.tricefal.core.login.SignupJpaRepository
import io.tricefal.core.okta.IamRegisterService
import io.tricefal.core.right.AccessRight
import io.tricefal.core.twilio.SmsMessage
import io.tricefal.core.twilio.SmsService
import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class SignupAdapter(private var repository: SignupJpaRepository,
                    val registrationService: IamRegisterService,
                    val mailService: EmailService,
                    val smsService: SmsService,
                    val keycloakRegisterService: IamRegisterService) : ISignupAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun signup(signup: SignupDomain): SignupDomain {
        repository.findByUsername(signup.username).ifPresent {
            throw DuplicateKeyException("a signup with username ${signup.username} is already taken")
        }
        val signupEntity = repository.save(toEntity(signup))
        return fromEntity(signupEntity)
    }

    override fun findByUsername(username: String): Optional<SignupDomain> {
        return repository.findByUsername(username).map {
            fromEntity(it)
        }
    }

    override fun update(signup: SignupDomain): SignupDomain {
        val entity = toEntity(signup)
        repository.findByUsername(signup.username).ifPresent {
            entity.id = it.id
        }
        val signupEntity = repository.save(entity)
        return fromEntity(signupEntity)
    }

    override fun register(signup: SignupDomain): Boolean {
        return registrationService.register(signup)
    }

    override fun sendSms(notification: SignupNotificationDomain): Boolean {
        logger.info("Sending ans SMS")
        val message = SmsMessage.Builder()
                .from(notification.smsFrom!!)
                .to(notification.smsTo!!)
                .content(notification.smsContent!!)
                .build()
        val result = smsService.send(message).matches(Regex("^SM[a-z0-9]*"))
        logger.info("An SMS has been sent")
        return result
    }

    override fun sendEmail(notification: SignupNotificationDomain): Boolean {
        logger.info("Sending an email")
        val message = EmailMessage.Builder()
                .from(notification.emailFrom!!)
                .to(notification.emailTo!!)
                .subject(notification.emailSubject!!)
                .content(notification.emailContent!!)
                .emailTemplate(EmailTemplate.SIGNUP)
                .model(hashMapOf("greeting" to notification.emailGreeting!!, "content" to notification.emailContent!!))
                .build()
        mailService.send(message)
        logger.info("An Email has been sent")
        return true
    }

    override fun updateStatus(signup: SignupDomain): SignupDomain {
        keycloakRegisterService.addRole(signup.username, statusToRole[signup.status]!!)
        return update(signup)
    }

    val statusToRole = mapOf(
            Status.FREELANCE to AccessRight.AC_FREELANCE_READ,
            Status.EMPLOYEE to AccessRight.AC_COLLABORATOR_READ,
            Status.CLIENT to AccessRight.AC_CLIENT_READ
    )
}





