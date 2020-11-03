package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.notification.NotificationDomain
import org.slf4j.LoggerFactory

class SignupService(private var adapter: ISignupAdapter) : ISignupService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun signup(signup: SignupDomain,
                        notification: NotificationDomain): SignupStateDomain {
        adapter.findByUsername(signup.username).ifPresent {
            throw SignupUsernameUniquenessException("a signup with username ${signup.username} is already taken")
        }

        trySignup(signup, notification)

        return signup.state!!
    }

    private fun trySignup(signup: SignupDomain, notification: NotificationDomain) {
        try {
            signup.state = SignupStateDomain.Builder(signup.username)
                    .registered(register(signup))
                    .saved(save(signup))
                    .cguAccepted(signup.cguAcceptedVersion?.let { acceptCgu(signup, it) })
                    .emailSent(sendEmail(signup, notification))
                    .activationCodeSent(sendSms(signup, notification))
                    .build()
        } catch (ex: Exception) {
            logger.error("a signup with username ${signup.username} has failed. ex : ${ex.localizedMessage}")
            logger.error("${ex.stackTrace}")
            throw SignupUserRegistrationException("a signup with username ${signup.username} has failed. ")
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

    override fun delete(signup: SignupDomain) {
        try {
            signup.state?.deleted = true
            adapter.update(signup)
//            adapter.delete(signup.username)
        } catch (ex: Exception) {
            logger.error("failed to delete from persistence the signup for username ${signup.username}")
            throw SignupPersistenceException("failed to delete from persistence the signup for username ${signup.username}")
        }
    }

    override fun findByUsername(username: String): SignupDomain {
        if (username.isEmpty()) throw SignupUserNotFoundException("username is $username")
        return adapter.findByUsername(username)
                .filter{ it.state?.deleted == false }
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
        adapter.signupActivated(signup)
        return signup.state!!
    }

    override fun deactivate(signup: SignupDomain): SignupStateDomain {
        signup.state?.validated = false
        adapter.update(signup)
        return signup.state!!
    }

    override fun resendCode(signup: SignupDomain,
                        notification: NotificationDomain): SignupStateDomain {
        adapter.findByUsername(signup.username).orElseThrow {
            logger.error("a signup with username ${signup.username} is does not exist")
            throw SignupUsernameUniquenessException("a signup with username ${signup.username} does not exist")
        }

        tryResendCode(signup, notification)
        adapter.update(signup)
        return signup.state!!
    }

    private fun tryResendCode(signup: SignupDomain, notification: NotificationDomain) {
        try {
            signup.state = SignupStateDomain.Builder(signup.username)
                    .registered(signup.state?.registered)
                    .emailValidated(signup.state?.emailValidated)
                    .portraitUploaded(signup.state?.portraitUploaded)
                    .resumeUploaded(signup.state?.resumeUploaded)
                    .resumeLinkedinUploaded(signup.state?.resumeLinkedinUploaded)
                    .statusUpdated(signup.state?.statusUpdated)
                    .validated(signup.state?.validated)
                    .emailSent(
                            if (signup.state?.emailValidated == true) true
                            else adapter.sendEmail(notification)
                    )
                    .activationCodeSent(
                            if (signup.state?.activatedByCode == true) true
                            else adapter.sendSms(notification)
                    )
                    .build()

        } catch (ex: Exception) {
            logger.error("Resend activation code failed for username ${signup.username}")
            logger.error("${ex.stackTrace}")
            throw SignupResendActivationCodeException("Resend activation code failed for username ${signup.username}")
        }
    }

    override fun verifyByCode(signup: SignupDomain, code: String): SignupStateDomain {
        signup.state?.activatedByCode = signup.activationCode.equals(code)
        adapter.update(signup)
        return signup.state!!
    }

    override fun verifyByEmail(signup: SignupDomain, code: String): SignupStateDomain {
        signup.state?.emailValidated = signup.activationCode.equals(code)
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
            adapter.update(signup)
            return signup.state!!
        } catch (ex: Exception) {
            logger.error("failed to update the status of the signup for username ${signup.username}")
            throw SignupStatusUpdateException("failed to update the status of the signup for username ${signup.username}")
        }
    }

    private fun save(signup: SignupDomain) : Boolean {
        try {
            signup.state!!.saved = true
            adapter.save(signup)
            adapter.update(signup)
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

    private fun sendEmail(signup: SignupDomain, notification: NotificationDomain): Boolean {
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

    private fun sendSms(signup: SignupDomain, notification: NotificationDomain): Boolean {
        try {
            signup.state?.emailSent = true
            adapter.sendSms(notification)
            adapter.update(signup)
            return true
        } catch (ex: Exception) {
            logger.error("failed to send an sms for activation for username ${signup.username}")
            throw SignupSmsNotificationException("failed to send an sms for activation for username ${signup.username}")
        }
    }

}

class SignupPersistenceException(val s: String) : Throwable()
class SignupUserNotFoundException(val s: String) : Throwable()
class SignupUsernameUniquenessException(val s: String) : Throwable()
class SignupUserRegistrationException(val s: String) : Throwable()
class SignupResendActivationCodeException(val s: String) : Throwable()
class SignupEmailNotificationException(val s: String) : Throwable()
class SignupSmsNotificationException(val s: String) : Throwable()
class SignupCguAcceptException(val s: String) : Throwable()
class SignupStatusUpdateException(val s: String) : Throwable()
class SignupPortraitUploadException(val s: String) : Throwable()
