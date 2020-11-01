package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.notification.NotificationDomain
import java.util.*

interface ISignupAdapter {
    fun save(signup: SignupDomain): SignupDomain
    fun delete(username: String)
    fun findByUsername(username: String): Optional<SignupDomain>
    fun findAll(): List<SignupDomain>
    fun update(signup: SignupDomain): SignupDomain

    fun register(signup: SignupDomain) : Boolean
    fun unregister(username: String) : Boolean
    fun sendEmail(notification: NotificationDomain) : Boolean
    fun sendSms(notification: NotificationDomain) : Boolean
    fun updateStatus(signup: SignupDomain): SignupDomain

    // events
    fun cguAccepted(username: String, cguAcceptedVersion: String)
    fun statusUpdated(signup: SignupDomain)
    fun portraitUploaded(fileDomain: MetafileDomain)
    fun resumeUploaded(fileDomain: MetafileDomain)
    fun resumeLinkedinUploaded(fileDomain: MetafileDomain)
    fun signupActivated(signup: SignupDomain)
}