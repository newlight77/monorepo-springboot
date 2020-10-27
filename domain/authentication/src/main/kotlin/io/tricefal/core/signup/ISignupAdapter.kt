package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

interface ISignupAdapter {
    fun save(signup: SignupDomain): SignupDomain
    fun delete(username: String)
    fun findByUsername(username: String): Optional<SignupDomain>
    fun findAll(): List<SignupDomain>
    fun update(signup: SignupDomain): SignupDomain

    fun register(signup: SignupDomain) : Boolean
    fun sendEmail(notification: SignupNotificationDomain) : Boolean
    fun sendSms(notification: SignupNotificationDomain) : Boolean
    fun updateStatus(signup: SignupDomain): SignupDomain

    // events
    fun statusUpdated(signup: SignupDomain)
    fun portraitUploaded(fileDomain: MetafileDomain)
    fun resumeUploaded(fileDomain: MetafileDomain)
    fun resumeLinkedinUploaded(fileDomain: MetafileDomain)
}