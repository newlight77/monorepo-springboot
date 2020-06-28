package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

class SignupService(private var adapter: ISignupAdapter) : ISignupService {

    override fun signup(signup: SignupDomain, notification: SignupNotificationDomain): SignupStateDomain {
        signup.state = SignupStateDomain.Builder(signup.username)
                .oktaRegistered(adapter.oktaRegister(signup))
                .emailSent(adapter.sendEmail(notification))
                .activationCodeSent(adapter.sendSms(notification))
                .build()

        adapter.signup(signup)

        return signup.state!!
    }

    override fun findByUsername(username: String): Optional<SignupDomain> {
        return adapter.findByUsername(username)
    }

    override fun activate(signup: SignupDomain, code: String): SignupStateDomain {
        signup.state?.activatedByCode = signup.activationCode.equals(code)
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
        adapter.update(signup)
        return signup.state!!
    }

}