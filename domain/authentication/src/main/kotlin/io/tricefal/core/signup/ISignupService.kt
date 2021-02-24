package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.notification.MetaNotificationDomain

interface ISignupService {
    fun signup(signup: SignupDomain, metaNotification: MetaNotificationDomain) : SignupStateDomain
    fun findByUsername(username: String): SignupDomain
    fun findAll(): List<SignupDomain>
    fun delete(signup: SignupDomain, authorizationCode: String?)
    fun activate(signup: SignupDomain, metaNotification: MetaNotificationDomain): SignupStateDomain
    fun deactivate(signup: SignupDomain): SignupStateDomain
    fun resendCodeBySmsForValidation(signup: SignupDomain, metaNotification: MetaNotificationDomain): Boolean
    fun resendCodeByEmailForValidation(signup: SignupDomain, metaNotification: MetaNotificationDomain): Boolean
    fun verifyByCode(signup: SignupDomain, code: String): SignupStateDomain
    fun verifyByCodeFromToken(token: String): SignupStateDomain

    fun resumeUploaded(signup: SignupDomain, metafileDomain: MetafileDomain): SignupStateDomain
    fun resumeLinkedinUploaded(signup: SignupDomain, metafileDomain: MetafileDomain): SignupStateDomain
    fun updateStatus(signup: SignupDomain, status: Status, metaNotification: MetaNotificationDomain): SignupStateDomain
    fun companyCompleted(username: String): SignupStateDomain

    fun profileResumeUploaded(username: String, filename: String): SignupDomain
    fun profileResumeLinkedinUploaded(username: String, filename: String): SignupDomain
    fun addComment(targetUsername: String, comment: CommentDomain): CommentDomain
}