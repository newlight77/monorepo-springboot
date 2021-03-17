package io.tricefal.core.signup

import io.tricefal.core.notification.EmailNotificationDomain
import io.tricefal.core.notification.SmsNotificationDomain
import io.tricefal.core.right.AccessRight
import java.util.*

interface SignupDataAdapter {
    fun save(signup: SignupDomain): SignupDomain
    fun delete(username: String)
    fun findByUsername(username: String): Optional<SignupDomain>
    fun findAll(): List<SignupDomain>
    fun update(signup: SignupDomain): Optional<SignupDomain>

    fun register(signup: SignupDomain) : Boolean
    fun unregister(username: String) : Boolean
    fun sendEmail(notification: EmailNotificationDomain) : Boolean
    fun sendSms(username: String, notification: SmsNotificationDomain) : Boolean

    // events
    fun saved(signup: SignupDomain)
    fun cguAccepted(username: String, cguAcceptedVersion: String)
    fun registered(signup: SignupDomain)
    fun smsSent(signup: SignupDomain)
    fun emailSent(signup: SignupDomain)
    fun resumeUploaded(username: String, filename: String)
    fun resumeLinkedinUploaded(username: String, filename: String)
    fun statusUpdated(signup: SignupDomain)

    fun validated(signup: SignupDomain)
    fun unvalidated(signup: SignupDomain)
    fun smsValidated(signup: SignupDomain)
    fun emailValidated(signup: SignupDomain)
    fun missionCompleted(signup: SignupDomain)
    fun companyCompleted(signup: SignupDomain)

    fun commented(targetUsername: String, comment: CommentDomain)
    fun softDeleted(username: String)

    fun assignRole(username: String, accessRight: AccessRight)
}