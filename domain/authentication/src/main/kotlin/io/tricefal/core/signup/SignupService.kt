package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

class SignupService(private var adapter: ISignupAdapter) : ISignupService {

    override fun signup(signup: SignupDomain,
                        notification: SignupNotificationDomain): SignupStateDomain {
        adapter.findByUsername(signup.username).ifPresent {
            throw UsernameUniquenessException("a signup with username ${signup.username} is already taken")
        }

        signup.state = SignupStateDomain.Builder(signup.username)
                .registered(adapter.register(signup))
                .emailSent(adapter.sendEmail(notification))
                .activationCodeSent(adapter.sendSms(notification))
                .build()

        adapter.signup(signup)

        return signup.state!!
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
