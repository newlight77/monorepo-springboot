package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

interface ISignupService {
    fun signup(signup: SignupDomain, notification: SignupNotificationDomain) : SignupStateDomain
    fun findByUsername(username: String): Optional<SignupDomain>
    fun activate(signup: SignupDomain, code: String): SignupStateDomain
    fun verifyFromToken(signup: SignupDomain, code: String): SignupStateDomain
    fun resumeUploaded(signup: SignupDomain, resumeFileDomain: MetafileDomain): SignupStateDomain
    fun updateStatus(signup: SignupDomain, status: Status): SignupStateDomain
}