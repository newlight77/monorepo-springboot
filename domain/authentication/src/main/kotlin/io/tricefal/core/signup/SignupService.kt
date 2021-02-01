package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.notification.MetaNotificationDomain
import io.tricefal.core.notification.SmsNotificationDomain
import io.tricefal.core.right.AccessRight
import org.slf4j.LoggerFactory
import java.security.SecureRandom
import java.util.*


class SignupService(private var dataAdapter: SignupDataAdapter) : ISignupService, SignupNotificationFactory() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun signup(signup: SignupDomain,
                        metaNotification: MetaNotificationDomain): SignupStateDomain {
        dataAdapter.findByUsername(signup.username).ifPresent {
            if (it.state?.registered == true
                    || it.state?.smsValidated == true
                    || it.state?.validated == true)
                throw SignupUsernameUniquenessException("a signup with username ${signup.username} is already taken")
        }

        val activationCode = generateCode()
        signup.activationCode = activationCode
        signup.activationToken = encodeToken(signup.username, activationCode)

        return SignupStateDomain.Builder(signup.username)
                .saved(save(signup))
                .registered(register(signup))
                .cguAccepted(signup.cguAcceptedVersion?.let { acceptCgu(signup, it) })
                .emailSent(sendSignupEmailNotification(signup, metaNotification))
                .smsSent(sendSms(signup, signupSmsNotification(signup, metaNotification)))
                .build()
    }

    override fun resendCodeBySmsForValidation(signup: SignupDomain,
                                              metaNotification: MetaNotificationDomain): SignupStateDomain {
        dataAdapter.findByUsername(signup.username).orElseThrow {
            logger.error("a signup with username ${signup.username} does not exist")
            throw SignupNotFoundException("a signup with username ${signup.username} does not exist")
        }

        val activationCode = generateCode()
        signup.activationCode = activationCode
        signup.activationToken = "${encode(activationCode)}.${encode(signup.username)}"

        return SignupStateDomain.Builder(signup.username)
            .saved(signup.state?.saved)
            .registered(signup.state?.registered)
            .emailValidated(signup.state?.emailValidated)
            .resumeUploaded(signup.state?.resumeUploaded)
            .resumeLinkedinUploaded(signup.state?.resumeLinkedinUploaded)
            .statusUpdated(signup.state?.statusUpdated)
            .validated(signup.state?.validated)
            .emailSent(
                signup.state?.emailValidated ?: dataAdapter.sendEmail(singupEmailNotification(signup, metaNotification))
            )
            .smsSent(sendSms(signup, signupSmsNotification(signup, metaNotification)))
            .build()
    }

    override fun resendCodeByEmailForValidation(signup: SignupDomain,
                                                metaNotification: MetaNotificationDomain): SignupStateDomain {
        dataAdapter.findByUsername(signup.username).orElseThrow {
            logger.error("a signup with username ${signup.username} does not exist")
            throw SignupNotFoundException("a signup with username ${signup.username} does not exist")
        }

        val activationCode = generateCode()
        signup.activationCode = activationCode
        signup.activationToken = "${encode(activationCode)}.${encode(signup.username)}"

        return SignupStateDomain.Builder(signup.username)
                .saved(signup.state?.saved)
                .registered(signup.state?.registered)
                .emailValidated(signup.state?.emailValidated)
                .resumeUploaded(signup.state?.resumeUploaded)
                .resumeLinkedinUploaded(signup.state?.resumeLinkedinUploaded)
                .statusUpdated(signup.state?.statusUpdated)
                .validated(signup.state?.validated)
                .emailSent(dataAdapter.sendEmail(singupEmailNotification(signup, metaNotification)))
                .smsSent(
                    signup.state?.smsValidated ?: sendSms(signup, signupSmsNotification(signup, metaNotification))
                )
                .build()
    }

    override fun delete(signup: SignupDomain, authorizationCode: String?) {
        try {
            if (authorizationCode?.isNotBlank() == true) {
                if (signup.activationCode == authorizationCode) dataAdapter.delete(signup.username)
            } else {
                // soft deletion
                dataAdapter.softDelete(signup.username)
                signup.state?.deleted = true
                dataAdapter.update(signup)
                    .orElseThrow { SignupDeletionException("Failed to update the signup state after soft deletion") }
            }
        } catch (ex: Exception) {
            logger.error("failed to delete from persistence the signup for username ${signup.username}")
            throw SignupPersistenceException("failed to delete from persistence the signup for username ${signup.username}", ex)
        }
    }

    override fun findByUsername(username: String): SignupDomain {
        if (username.isEmpty()) throw SignupUserNotFoundException("username is $username")
        return dataAdapter.findByUsername(username)
                .orElseThrow {
                    logger.error("resource not found for username $username")
                    SignupUserNotFoundException("resource not found for username $username")
                }
    }

    override fun findAll(): List<SignupDomain> {
        return dataAdapter.findAll()
    }

    override fun activate(signup: SignupDomain, metaNotification: MetaNotificationDomain): SignupStateDomain {
        try {
            signup.state?.validated = true
            dataAdapter.update(signup)
            dataAdapter.sendEmail(activatedEmailNotification(signup, metaNotification))
            assignRoles(signup, statusToReadWriteRole[signup.status])
            dataAdapter.validated(signup)
            return signup.state!!
        } catch (ex: Throwable) {
            logger.error("Failed to update the signup state after activation for username ${signup.username}")
            throw SignupActivationException("Failed to update the signup state after activation for username ${signup.username}")
        }
    }

    override fun deactivate(signup: SignupDomain): SignupStateDomain {
        signup.state?.validated = false
        dataAdapter.unvalidated(signup)
        dataAdapter.update(signup)
            .orElseThrow { SignupDeactivationException("Failed to update the signup state after activation") }
        return signup.state!!
    }

    override fun verifyByCode(signup: SignupDomain, code: String): SignupStateDomain {
        signup.state?.smsValidated = signup.activationCode?.toInt() == code.toInt()
        if (signup.state?.smsValidated != true) {
            logger.error("failed to active by code fur user ${signup.username}")
            throw SignupVerificationByCodeException("failed to active by code fur user ${signup.username}")
        }
        dataAdapter.smsValidated(signup)
        dataAdapter.update(signup)
        return signup.state!!
    }

    override fun verifyByCodeFromToken(token: String): SignupStateDomain {
        // the received token has 3 parts, the third is intentionally ignored as overfilled
        val values = token.split(".")
        if (values.size < 3) throw SignupVerificationByCodeFromTokenException("verify email by token : the token is invalid")
        val activationCode = decode(values[0])
        val username = decode(values[1])

        val signup = dataAdapter.findByUsername(username).orElseThrow {
            logger.error("a signup with username $username does not exist")
            throw SignupNotFoundException("a signup with username $username does not exist")
        }

        signup.state?.emailValidated = signup.activationCode.equals(activationCode)

        if (signup.state?.emailValidated != true) {
            logger.warn("the token is invalid for verification by email : token=$token")
            throw SignupVerificationByCodeFromTokenException("the token is invalid for verification by email : token=$token")
        } else {
            logger.info("successfully verified the token by email for user $username")
        }

        dataAdapter.emailValidated(signup)
        dataAdapter.update(signup)
            .orElseThrow { SignupVerificationByCodeFromTokenException("Failed to update the signup state after verification by code") }
        return signup.state!!
    }

    override fun resumeUploaded(signup: SignupDomain, metafileDomain: MetafileDomain): SignupStateDomain {
        try {
            signup.resumeFilename = metafileDomain.filename
            signup.state!!.resumeUploaded = true
            dataAdapter.update(signup)
                .orElseThrow { SignupResumeUploadException("failed to update the signup with resume upload of the signup for username ${signup.username}")}
            dataAdapter.resumeUploaded(metafileDomain)
            return signup.state!!
        } catch (ex: Throwable) {
            logger.error("failed to update the signup with resume upload of the signup for username ${signup.username}")
            throw SignupResumeUploadException("failed to update the signup with resume upload of the signup for username ${signup.username}", ex)
        }
    }

    override fun resumeLinkedinUploaded(signup: SignupDomain, metafileDomain: MetafileDomain): SignupStateDomain {
        try {
            signup.resumeLinkedinFilename = metafileDomain.filename
            signup.state!!.resumeLinkedinUploaded = true
            dataAdapter.update(signup)
                .orElseThrow { SignupLinkedinResumeUploadException("failed to update the signup with linkedin upload of the signup for username ${signup.username}")}
            dataAdapter.resumeLinkedinUploaded(metafileDomain)
            return signup.state!!
        } catch (ex: Throwable) {
            logger.error("failed to update the signup with linkedin resume upload of the signup for username ${signup.username}")
            throw SignupLinkedinResumeUploadException("failed to update the signup with linkedin resume upload of the signup for username ${signup.username}", ex)
        }
    }

    override fun updateStatus(signup: SignupDomain, status: Status, metaNotification: MetaNotificationDomain): SignupStateDomain {
        try {
            signup.status = status
            signup.state!!.statusUpdated = true
            dataAdapter.update(signup)
            dataAdapter.statusUpdated(signup)
            dataAdapter.sendEmail(waitingForActivationEmailNotification(signup, metaNotification))
            assignRoles(signup, statusToReadRole[status])
            return signup.state!!
        } catch (ex: Throwable) {
            logger.error("failed to update the status of the signup for username ${signup.username}")
            throw SignupStatusUpdateException("failed to update the status of the signup for username ${signup.username}", ex)
        }
    }

    override fun companyCompleted(username: String): SignupStateDomain {
        val signup = dataAdapter.findByUsername(username).orElseThrow {
            logger.error("a signup with username $username does not exist")
            throw SignupNotFoundException("a signup with username $username does not exist")
        }

        try {
            signup.state?.completed = true
            dataAdapter.update(signup)
            dataAdapter.companyCompleted(signup)
            return signup.state!!
        } catch (ex: Throwable) {
            logger.error("failed to update the state upon company completion of the signup for username ${signup.username}")
            throw SignupStatusUpdateException("failed to update the state of the signup upon company completion for username ${signup.username}", ex)
        }
    }

    override fun profileResumeUploaded(username: String, filename: String): SignupDomain {
        val signup = dataAdapter.findByUsername(username).orElseThrow {
            logger.error("a signup with username $username does not exist")
            throw SignupNotFoundException("a signup with username $username does not exist")
        }

        try {
            signup.state?.resumeUploaded = true
            signup.resumeFilename = filename
            dataAdapter.update(signup)
            return signup
        } catch (ex: Throwable) {
            logger.error("failed to update the signup and state upon resume upload for username ${signup.username}")
            throw SignupStatusUpdateException("failed to update the signup and state upon resume upload for username ${signup.username}", ex)
        }
    }

    override fun profileResumeLinkedinUploaded(username: String, filename: String): SignupDomain {
        val signup = dataAdapter.findByUsername(username).orElseThrow {
            logger.error("a signup with username $username does not exist")
            throw SignupNotFoundException("a signup with username $username does not exist")
        }

        try {
            signup.state?.resumeLinkedinUploaded = true
            signup.resumeLinkedinFilename = filename
            dataAdapter.update(signup)
            return signup
        } catch (ex: Throwable) {
            logger.error("failed to update the signup and state upon resume linkedin upload for username ${signup.username}")
            throw SignupStatusUpdateException("failed to update the signup and state upon resume linkedin upload for username ${signup.username}", ex)
        }
    }

    private fun register(signup: SignupDomain): Boolean {
        try {
            if (dataAdapter.register(signup)) {
                signup.state?.registered = true
                dataAdapter.update(signup)
                    .orElseThrow { SignupUserRegistrationException("failed to update the signup after register for username ${signup.username}")}
                return true
            }
            return false
        } catch (ex: Throwable) {
            logger.error("a signup with username ${signup.username} has failed. ex : ${ex.localizedMessage}")
            logger.error("${ex.stackTrace}")
            throw SignupUserRegistrationException("a signup with username ${signup.username} has failed. ", ex)
        }
    }

    private fun save(signup: SignupDomain) : Boolean {
        try {
            signup.state!!.saved = true
            dataAdapter.save(signup)
            return true
        } catch (ex: Throwable) {
            logger.error("failed to persist the signup for username ${signup.username}")
            throw SignupPersistenceException("failed to persist the signup for username ${signup.username}", ex)
        }
    }

    private fun acceptCgu(signup: SignupDomain, cguAcceptedVersion: String) : Boolean {
        try {
            signup.state?.cguAccepted = true
            dataAdapter.cguAccepted(signup.username, cguAcceptedVersion)
            dataAdapter.update(signup)
                .orElseThrow { SignupCguAcceptException("failed to update the signup after accepting cgu for username ${signup.username}")}
            return true
        } catch (ex: Throwable) {
            logger.error("failed to accept the cgu for username ${signup.username}")
            throw SignupCguAcceptException("failed to accept the cgu for username ${signup.username}", ex)
        }
    }

    private fun sendSignupEmailNotification(signup: SignupDomain, metaNotification: MetaNotificationDomain): Boolean {
        try {
            signup.state?.emailSent = true
            dataAdapter.sendEmail(singupEmailNotification(signup, metaNotification))
            dataAdapter.sendEmail(notifyAdminForActivation(signup, metaNotification))
            dataAdapter.emailSent(signup)
            dataAdapter.update(signup)
                .orElseThrow { SignupEmailNotificationException("failed to update the signup after sending email for username ${signup.username}")}
            return true
        } catch (ex: Throwable) {
            logger.error("failed to send an email for validation for username ${signup.username}")
            throw SignupEmailNotificationException("failed to send an email for validation for username ${signup.username}", ex)
        }
    }

    private fun sendSms(signup: SignupDomain, notification: SmsNotificationDomain): Boolean {
        try {
            signup.state?.smsSent = true
            dataAdapter.sendSms(signup.username, notification)
            dataAdapter.update(signup)
                .orElseThrow { SignupSmsNotificationException("failed to update the signup after sending sms for username ${signup.username}")}
            return true
        } catch (ex: Throwable) {
            logger.error("failed to send an sms for activation for username ${signup.username}")
            throw SignupSmsNotificationException("failed to send an sms for activation for username ${signup.username}", ex)
        }
    }

    private fun assignRoles(signup: SignupDomain, roles: List<AccessRight>?) {
        try {
            roles?.forEach { dataAdapter.assignRole(signup.username, it) }
        } catch (ex: Throwable) {
            logger.error("Failed to assign the role ${statusToReadRole[signup.status]} to user ${signup.username}")
            throw SignupRoleAssignationException("Failed to assign the role ${statusToReadRole[signup.status]} to user ${signup.username}", ex)
        }
    }

    private val statusToReadRole = mapOf(
        Status.FREELANCE to listOf(AccessRight.AC_FREELANCE_READ, AccessRight.AC_TRICEFAL_READ),
        Status.FREELANCE_WITH_MISSION to listOf(AccessRight.AC_FREELANCE_READ, AccessRight.AC_TRICEFAL_READ),
        Status.EMPLOYEE to listOf(AccessRight.AC_COLLABORATOR_READ, AccessRight.AC_TRICEFAL_READ),
        Status.CLIENT to listOf(AccessRight.AC_CLIENT_READ, AccessRight.AC_TRICEFAL_READ)
    )

    private val statusToReadWriteRole = mapOf(
            Status.FREELANCE to listOf(AccessRight.AC_FREELANCE_READ, AccessRight.AC_FREELANCE_WRITE, AccessRight.AC_TRICEFAL_READ),
            Status.FREELANCE_WITH_MISSION to listOf(AccessRight.AC_FREELANCE_READ, AccessRight.AC_FREELANCE_WRITE, AccessRight.AC_TRICEFAL_READ),
            Status.EMPLOYEE to listOf(AccessRight.AC_COLLABORATOR_READ, AccessRight.AC_COLLABORATOR_WRITE, AccessRight.AC_TRICEFAL_READ),
            Status.CLIENT to listOf(AccessRight.AC_CLIENT_READ, AccessRight.AC_CLIENT_WRTIE, AccessRight.AC_TRICEFAL_READ)
    )

    fun generateCode(): String {
        val code = SecureRandom().nextGaussian().toString().takeLast(6)
        logger.info("an activation code has been generated $code")
        return code
    }

    fun encodeToken(username: String, activationCode: String): String {
        val token = "${encode(activationCode)}.${encode(username)}"
        logger.info("an activation token has been generated $token")
        return token
    }

    fun encode(code: String): String = Base64.getUrlEncoder()
            .encodeToString(code.toByteArray())

    fun decode(code: String): String = String(Base64.getUrlDecoder().decode(code.toByteArray()))

}

class SignupDeletionException(val s: String) : Throwable()
class SignupActivationException(val s: String) : Throwable()
class SignupDeactivationException(val s: String) : Throwable()

class SignupNotFoundException(val s: String) : Throwable()
class SignupPersistenceException(val s: String, ex: Throwable?) : Throwable(ex)
class SignupUserNotFoundException(val s: String) : Throwable()
class SignupUsernameUniquenessException(val s: String) : Throwable()
class SignupUserRegistrationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class SignupVerificationByCodeException(val s: String) : Throwable()
class SignupVerificationByCodeFromTokenException(val s: String) : Throwable()
class SignupEmailNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class SignupSmsNotificationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class SignupCguAcceptException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class SignupStatusUpdateException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class SignupResumeUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class SignupLinkedinResumeUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class SignupRoleAssignationException(val s: String, ex: Throwable?) : Throwable(ex)
class SignupResourceBundleMissingKeyException(val s: String) : Throwable()
