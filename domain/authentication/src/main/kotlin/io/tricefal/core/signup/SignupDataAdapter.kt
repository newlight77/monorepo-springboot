package io.tricefal.core.signup

import io.tricefal.core.metafile.MetafileDomain
import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.SmsNotificationDomain
import io.tricefal.core.right.AccessRight
import java.util.*

interface SignupDataAdapter {
    fun save(signup: SignupDomain): SignupDomain
    fun delete(username: String)
    fun softDelete(username: String)
    fun findByUsername(username: String): Optional<SignupDomain>
    fun findAll(): List<SignupDomain>
    fun update(signup: SignupDomain): Optional<SignupDomain>

    fun register(signup: SignupDomain) : Boolean
    fun unregister(username: String) : Boolean
    fun sendEmail(notification: EmailNotificationDomain) : Boolean
    fun sendSms(username: String, notification: SmsNotificationDomain) : Boolean

    // events
    fun cguAccepted(username: String, cguAcceptedVersion: String)
    fun emailSent(signup: SignupDomain)
    fun statusUpdated(signup: SignupDomain)
    fun portraitUploaded(fileDomain: MetafileDomain)
    fun resumeUploaded(fileDomain: MetafileDomain)
    fun resumeLinkedinUploaded(fileDomain: MetafileDomain)

    fun assignRole(username: String, accessRight: AccessRight)
}