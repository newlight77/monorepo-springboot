package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import org.slf4j.LoggerFactory

class SignupService(private var adapter: ISignupAdapter) : ISignupService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun signup(signup: SignupDomain,
                        notification: SignupNotificationDomain): SignupStateDomain {
        adapter.findByUsername(signup.username).ifPresent {
            throw UsernameUniquenessException("a signup with username ${signup.username} is already taken")
        }

        trySignup(signup, notification)

        adapter.save(signup)

        return signup.state!!
    }

    private fun trySignup(signup: SignupDomain, notification: SignupNotificationDomain) {
        try {
            signup.state = SignupStateDomain.Builder(signup.username)
                    .registered(adapter.register(signup))
                    .emailSent(adapter.sendEmail(notification))
                    .activationCodeSent(adapter.sendSms(notification))
                    .build()
        } catch (ex: Exception) {
            logger.error("a signup with username ${signup.username} has failed. ex : ${ex.localizedMessage}")
            logger.error("${ex.stackTrace}")
            throw UserRegistrationException("a signup with username ${signup.username} has failed. ")
        }
    }

    override fun delete(username: String) {
        return adapter.delete(username)
    }

    override fun findByUsername(username: String): SignupDomain {
        if (username.isEmpty()) throw UsernameNotFoundException("username is $username")
        return adapter.findByUsername(username)
                .orElseThrow {
                    logger.error("resource not found for username $username")
                    NotFoundException("resource not found for username $username")
                }
    }

    override fun findAll(): List<SignupDomain> {
        return adapter.findAll()
    }

    override fun activate(signup: SignupDomain): SignupStateDomain {
        signup.state?.validated = true
        adapter.update(signup)
        return signup.state!!
    }

    override fun deactivate(signup: SignupDomain): SignupStateDomain {
        signup.state?.validated = false
        adapter.update(signup)
        return signup.state!!
    }

    override fun resendCode(signup: SignupDomain,
                        notification: SignupNotificationDomain): SignupStateDomain {
        adapter.findByUsername(signup.username).ifPresent {
            logger.error("a signup with username ${signup.username} is already taken")
            throw UsernameUniquenessException("a signup with username ${signup.username} is already taken")
        }

        tryResendCode(signup, notification)

        adapter.update(signup)

        return signup.state!!
    }

    private fun tryResendCode(signup: SignupDomain, notification: SignupNotificationDomain) {
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
                            if (signup.state?.emailValidated!!) true
                            else adapter.sendEmail(notification)
                    )
                    .activationCodeSent(
                            if (signup.state?.activatedByCode!!) true
                            else adapter.sendSms(notification)
                    )
                    .build()

        } catch (ex: Exception) {
            logger.error("Resend activation code failed for username ${signup.username}")
            logger.error("${ex.stackTrace}")
            throw ResendActivationCodeException("Resend activation code failed for username ${signup.username}")
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

class NotFoundException(val s: String) : Throwable()
class UsernameNotFoundException(val s: String) : Throwable()
class UsernameUniquenessException(val s: String) : Throwable()
class UserRegistrationException(val s: String) : Throwable()
class ResendActivationCodeException(val s: String) : Throwable()
