package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import java.util.*
import org.slf4j.LoggerFactory

class SignupService(private var adapter: ISignupAdapter) : ISignupService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun signup(signup: SignupDomain,
                        notification: SignupNotificationDomain): SignupStateDomain {
        adapter.findByUsername(signup.username).ifPresent {
            throw UsernameUniquenessException("a signup with username ${signup.username} is already taken")
        }

        trySignup(signup, notification)

        adapter.signup(signup)

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
            throw UserRegistraationException("a signup with username ${signup.username} has failed. ")
        }
    }

    override fun findByUsername(username: String): Optional<SignupDomain> {
        return adapter.findByUsername(username)
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
                    .resumeUploaded(signup.state?.resumeUploaded)
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

    override fun resumeUploaded(signup: SignupDomain, resumeFileDomain: MetafileDomain): SignupStateDomain {
        signup.resumeFile = resumeFileDomain
        signup.state!!.resumeUploaded = true
        adapter.update(signup)
        return signup.state!!
    }

    override fun updateStatus(signup: SignupDomain, status: Status): SignupStateDomain {
        signup.status = status
        signup.state!!.statusUpdated = true
        adapter.updateStatus(signup)
        return signup.state!!
    }

}

class UsernameUniquenessException(val s: String) : Throwable()
class UserRegistraationException(val s: String) : Throwable()
class ResendActivationCodeException(val s: String) : Throwable()
