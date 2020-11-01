package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.notification.NotificationDomain

interface ISignupService {
    fun signup(signup: SignupDomain, notification: NotificationDomain) : SignupStateDomain
    fun findByUsername(username: String): SignupDomain
    fun findAll(): List<SignupDomain>
    fun delete(username: String)
    fun activate(signup: SignupDomain): SignupStateDomain
    fun deactivate(signup: SignupDomain): SignupStateDomain
    fun resendCode(signup: SignupDomain, notification: NotificationDomain): SignupStateDomain
    fun verifyByCode(signup: SignupDomain, code: String): SignupStateDomain
    fun verifyByEmail(signup: SignupDomain, code: String): SignupStateDomain

    fun portraitUploaded(signup: SignupDomain, metafileDomain: MetafileDomain): SignupStateDomain
    fun resumeUploaded(signup: SignupDomain, metafileDomain: MetafileDomain): SignupStateDomain
    fun resumeLinkedinUploaded(signup: SignupDomain, metafileDomain: MetafileDomain): SignupStateDomain
    fun updateStatus(signup: SignupDomain, status: Status): SignupStateDomain
}