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
                    .registered(adapter.register(signup))
                    .saved(save(signup))
                    .cguAccepted(signup.cguAcceptedVersion?.let { acceptCgu(signup.username,it) })
                    .emailSent(adapter.sendEmail(notification))
                    .activationCodeSent(adapter.sendSms(notification))
                    .build()
        } catch (ex: Exception) {
            logger.error("a signup with username ${signup.username} has failed. ex : ${ex.localizedMessage}")
            logger.error("${ex.stackTrace}")
            throw SignupUserRegistrationException("a signup with username ${signup.username} has failed. ")
        }
    }

    override fun delete(username: String) {
        try {
            adapter.delete(username)
        } catch (ex: Exception) {
            logger.error("failed to delete from persistence the signup for username $username")
            throw SignupPersistenceException("failed to delete from persistence the signup for username $username")
        }
    }

    private fun save(signup: SignupDomain) : Boolean {
        try {
            adapter.save(signup)
        } catch (ex: Exception) {
            logger.error("failed to persist the signup for username ${signup.username}")
            throw SignupPersistenceException("failed to persist the signup for username ${signup.username}")
        }
        return true
    }

    private fun acceptCgu(username: String, cguAcceptedVersion: String) : Boolean {
        try {
            adapter.cguAccepted(username, cguAcceptedVersion)
        } catch (ex: Exception) {
            logger.error("failed to accept the cgu for username $username")
            throw SignupPersistenceException("failed to accept the cgu for username $username")
        }
        return true
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
        signup.resumeFile = metafileDomain
        signup.state!!.portraitUploaded = true
        adapter.update(signup)
        adapter.portraitUploaded(metafileDomain)
        return signup.state!!
    }

    override fun resumeUploaded(signup: SignupDomain, metafileDomain: MetafileDomain): SignupStateDomain {
        signup.resumeFile = metafileDomain
        signup.state!!.resumeUploaded = true
        adapter.update(signup)
        adapter.resumeUploaded(metafileDomain)
        return signup.state!!
    }

    override fun resumeLinkedinUploaded(signup: SignupDomain, metafileDomain: MetafileDomain): SignupStateDomain {
        signup.resumeFile = metafileDomain
        signup.state!!.resumeLinkedinUploaded = true
        adapter.update(signup)
        adapter.resumeLinkedinUploaded(metafileDomain)
        return signup.state!!
    }

    override fun updateStatus(signup: SignupDomain, status: Status): SignupStateDomain {
        signup.status = status
        signup.state!!.statusUpdated = true
        adapter.updateStatus(signup)
        adapter.statusUpdated(signup)
        return signup.state!!
    }

}

class SignupPersistenceException(val s: String) : Throwable()
class SignupUserNotFoundException(val s: String) : Throwable()
class SignupUsernameUniquenessException(val s: String) : Throwable()
class SignupUserRegistrationException(val s: String) : Throwable()
class SignupResendActivationCodeException(val s: String) : Throwable()
