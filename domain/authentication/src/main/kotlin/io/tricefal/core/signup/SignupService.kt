package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.MetaNotificationDomain
import io.tricefal.core.notification.SmsNotificationDomain
import io.tricefal.core.right.AccessRight
import org.slf4j.LoggerFactory
import java.security.SecureRandom
import java.text.MessageFormat
import java.util.*


class SignupService(private var adapter: ISignupAdapter) : ISignupService, SignupNotificationFactory() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun signup(signup: SignupDomain,
                        metaNotification: MetaNotificationDomain): SignupStateDomain {
        adapter.findByUsername(signup.username).ifPresent {
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
                .emailSent(sendEmail(signup, emailNotification(signup, metaNotification)))
//                .smsSent(sendSms(signup, smsNotification(signup, metaNotification)))
                .build()
    }

    override fun resendCode(signup: SignupDomain,
                            metaNotification: MetaNotificationDomain): SignupStateDomain {
        adapter.findByUsername(signup.username).orElseThrow {
            logger.error("a signup with username ${signup.username} is does not exist")
            throw SignupNotFoundException("a signup with username ${signup.username} does not exist")
        }

        val activationCode = generateCode()
        signup.activationCode = activationCode
        signup.activationToken = "${encode(activationCode)}.${encode(signup.username)}"

        return SignupStateDomain.Builder(signup.username)
                .saved(signup.state?.saved)
                .registered(signup.state?.registered)
                .emailValidated(signup.state?.emailValidated)
                .portraitUploaded(signup.state?.portraitUploaded)
                .resumeUploaded(signup.state?.resumeUploaded)
                .resumeLinkedinUploaded(signup.state?.resumeLinkedinUploaded)
                .statusUpdated(signup.state?.statusUpdated)
                .validated(signup.state?.validated)
                .emailSent(
                        if (signup.state?.emailValidated == true) true
                        else sendEmail(signup, emailNotification(signup, metaNotification))
                )
                .smsSent(
                        if (signup.state?.smsValidated == true) true
                        else sendSms(signup, smsNotification(signup, metaNotification))
                )
                .build()
    }

    override fun delete(signup: SignupDomain, authorizationCode: String?) {
        try {
            if (authorizationCode?.isNotBlank() == true) {
                if (signup.activationCode == authorizationCode) adapter.delete(signup.username)
            } else {
                // soft deletion
                signup.state?.deleted = true
                adapter.update(signup)
            }
        } catch (ex: Exception) {
            logger.error("failed to delete from persistence the signup for username ${signup.username}")
            throw SignupPersistenceException("failed to delete from persistence the signup for username ${signup.username}")
        }
    }

    override fun findByUsername(username: String): SignupDomain {
        if (username.isEmpty()) throw SignupUserNotFoundException("username is $username")
        return adapter.findByUsername(username)
                .orElseThrow {
                    logger.error("resource not found for username $username")
                    SignupUserNotFoundException("resource not found for username $username")
                }
    }

    override fun findAll(): List<SignupDomain> {
        return adapter.findAll()
    }

    override fun activate(signup: SignupDomain): SignupStateDomain {
        signup.state?.validated = true
        adapter.update(signup)
        assignRoles(signup, statusToReadWriteRole[signup.status])
        return signup.state!!
    }

    override fun deactivate(signup: SignupDomain): SignupStateDomain {
        signup.state?.validated = false
        adapter.update(signup)
        return signup.state!!
    }

    override fun verifyByCode(signup: SignupDomain, code: String): SignupStateDomain {
        signup.state?.smsValidated = signup.activationCode?.toInt() == code.toInt()
        if (signup.state?.smsValidated != true) {
            logger.error("failed to active by code fur user ${signup.username}")
            throw SignupActivationByCodeException("failed to active by code fur user ${signup.username}")
        }
        adapter.update(signup)
        return signup.state!!
    }

    override fun verifyByCodeFromToken(token: String): SignupStateDomain {
        // the received token has 3 parts, the third is intentionally ignored as overfilled
        val values = token.split(".")
        if (values.size < 3) throw SignupActivationByEmailException("verify email by token : the token is invalid")
        val activationCode = decode(values[0])
        val username = decode(values[1])

        val signup = adapter.findByUsername(username).orElseThrow {
            logger.error("a signup with username $username is does not exist")
            throw SignupNotFoundException("a signup with username $username does not exist")
        }

        signup.state?.emailValidated = signup.activationCode.equals(activationCode)

        if (signup.state?.emailValidated != true) {
            logger.warn("the token is invalid for activation by email : token=$token")
            throw SignupActivationByCodeException("the token is invalid for activation by email : token=$token")
        } else {
            logger.info("successfully verified the token by email for user $username")
        }

        adapter.update(signup)
        return signup.state!!
    }

    override fun portraitUploaded(signup: SignupDomain, metafileDomain: MetafileDomain): SignupStateDomain {
        try {
            signup.resumeFile = metafileDomain
            signup.state!!.portraitUploaded = true
            adapter.update(signup)
            adapter.portraitUploaded(metafileDomain)
            return signup.state!!
        } catch (ex: Exception) {
            logger.error("failed to update the signup with portrait upload of the signup for username ${signup.username}")
            throw SignupPortraitUploadException("failed to update the signup with portrait upload of the signup for username ${signup.username}")
        }
    }

    override fun resumeUploaded(signup: SignupDomain, metafileDomain: MetafileDomain): SignupStateDomain {
        try {
            signup.resumeFile = metafileDomain
            signup.state!!.resumeUploaded = true
            adapter.update(signup)
            adapter.resumeUploaded(metafileDomain)
            return signup.state!!
        } catch (ex: Exception) {
            logger.error("failed to update the signup with resume upload of the signup for username ${signup.username}")
            throw SignupPortraitUploadException("failed to update the signup with resume upload of the signup for username ${signup.username}")
        }
    }

    override fun resumeLinkedinUploaded(signup: SignupDomain, metafileDomain: MetafileDomain): SignupStateDomain {
        try {
            signup.resumeFile = metafileDomain
            signup.state!!.resumeLinkedinUploaded = true
            adapter.update(signup)
            adapter.resumeLinkedinUploaded(metafileDomain)
            return signup.state!!
        } catch (ex: Exception) {
            logger.error("failed to update the signup with linkedin resume upload of the signup for username ${signup.username}")
            throw SignupPortraitUploadException("failed to update the signup with linkedin resume upload of the signup for username ${signup.username}")
        }
    }

    override fun updateStatus(signup: SignupDomain, status: Status): SignupStateDomain {
        try {
            signup.status = status
            signup.state!!.statusUpdated = true
            adapter.updateStatus(signup)
            adapter.statusUpdated(signup)
            assignRoles(signup, statusToReadRole[status])
            adapter.update(signup)
            return signup.state!!
        } catch (ex: Exception) {
            logger.error("failed to update the status of the signup for username ${signup.username}")
            throw SignupStatusUpdateException("failed to update the status of the signup for username ${signup.username}")
        }
    }

    private fun register(signup: SignupDomain): Boolean {
        try {
            if (adapter.register(signup)) {
                signup.state?.registered = true
                adapter.update(signup)
                return true
            }
            return false
        } catch (ex: Exception) {
            logger.error("a signup with username ${signup.username} has failed. ex : ${ex.localizedMessage}")
            logger.error("${ex.stackTrace}")
            throw SignupUserRegistrationException("a signup with username ${signup.username} has failed. ")
        }
    }

    private fun save(signup: SignupDomain) : Boolean {
        try {
            signup.state!!.saved = true
            adapter.save(signup)
            return true
        } catch (ex: Exception) {
            logger.error("failed to persist the signup for username ${signup.username}")
            throw SignupPersistenceException("failed to persist the signup for username ${signup.username}")
        }
    }

    private fun acceptCgu(signup: SignupDomain, cguAcceptedVersion: String) : Boolean {
        try {
            signup.state?.cguAccepted = true
            adapter.cguAccepted(signup.username, cguAcceptedVersion)
            adapter.update(signup)
            return true
        } catch (ex: Exception) {
            logger.error("failed to accept the cgu for username ${signup.username}")
            throw SignupCguAcceptException("failed to accept the cgu for username ${signup.username}")
        }
    }

    private fun sendEmail(signup: SignupDomain, notification: EmailNotificationDomain): Boolean {
        try {
            signup.state?.emailSent = true
            adapter.sendEmail(notification)
            adapter.update(signup)
            return true
        } catch (ex: Exception) {
            logger.error("failed to send an email for validation for username ${signup.username}")
            throw SignupEmailNotificationException("failed to send an email for validation for username ${signup.username}")
        }
    }

    private fun sendSms(signup: SignupDomain, notification: SmsNotificationDomain): Boolean {
        try {
            signup.state?.smsSent = true
            adapter.sendSms(notification)
            adapter.update(signup)
            return true
        } catch (ex: Exception) {
            logger.error("failed to send an sms for activation for username ${signup.username}")
            throw SignupSmsNotificationException("failed to send an sms for activation for username ${signup.username}")
        }
    }

    private fun assignRoles(signup: SignupDomain, roles: List<AccessRight>?) {
        try {
            roles?.forEach { adapter.assignRole(signup.username, it) }
        } catch (ex: Exception) {
            logger.error("Failed to assign the role ${statusToReadRole[signup.status]} to user ${signup.username}")
            throw SignupRoleAssignationException("Failed to assign the role ${statusToReadRole[signup.status]} to user ${signup.username}")
        }
    }

    private val statusToReadRole = mapOf(
            Status.FREELANCE to listOf(AccessRight.AC_FREELANCE_READ, AccessRight.AC_FREELANCE_WRITE),
            Status.EMPLOYEE to listOf(AccessRight.AC_COLLABORATOR_READ, AccessRight.AC_COLLABORATOR_WRITE),
            Status.CLIENT to listOf(AccessRight.AC_CLIENT_READ, AccessRight.AC_CLIENT_WRTIE)
    )

    private val statusToReadWriteRole = mapOf(
            Status.FREELANCE to listOf(AccessRight.AC_FREELANCE_READ, AccessRight.AC_FREELANCE_WRITE),
            Status.EMPLOYEE to listOf(AccessRight.AC_COLLABORATOR_READ, AccessRight.AC_COLLABORATOR_WRITE),
            Status.CLIENT to listOf(AccessRight.AC_CLIENT_READ, AccessRight.AC_CLIENT_WRTIE)
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

class SignupNotFoundException(val s: String) : Throwable()
class SignupPersistenceException(val s: String) : Throwable()
class SignupUserNotFoundException(val s: String) : Throwable()
class SignupUsernameUniquenessException(val s: String) : Throwable()
class SignupUserRegistrationException(val s: String) : Throwable()
class SignupActivationByCodeException(val s: String) : Throwable()
class SignupActivationByEmailException(val s: String) : Throwable()
class SignupEmailNotificationException(val s: String) : Throwable()
class SignupSmsNotificationException(val s: String) : Throwable()
class SignupCguAcceptException(val s: String) : Throwable()
class SignupStatusUpdateException(val s: String) : Throwable()
class SignupPortraitUploadException(val s: String) : Throwable()
class SignupRoleAssignationException(val s: String) : Throwable()
class SignupResourceBundleMissingKeyException(val s: String) : Throwable()
