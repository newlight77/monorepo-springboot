package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

interface ISignupService {
    fun signup(signup: SignupDomain, notification: SignupNotificationDomain) : SignupStateDomain
    fun findByUsername(username: String): SignupDomain
    fun findAll(): List<SignupDomain>
    fun delete(username: String)
    fun activate(signup: SignupDomain): SignupStateDomain
    fun deactivate(signup: SignupDomain): SignupStateDomain
    fun resendCode(signup: SignupDomain, notification: SignupNotificationDomain): SignupStateDomain
    fun verifyByCode(signup: SignupDomain, code: String): SignupStateDomain
    fun verifyByEmail(signup: SignupDomain, code: String): SignupStateDomain
    fun portraitUploaded(signup: SignupDomain, portraitFileDomain: MetafileDomain): SignupStateDomain
    fun resumeUploaded(signup: SignupDomain, resumeFileDomain: MetafileDomain): SignupStateDomain
    fun refUploaded(signup: SignupDomain, refFileDomain: MetafileDomain): SignupStateDomain
    fun updateStatus(signup: SignupDomain, status: Status): SignupStateDomain
}