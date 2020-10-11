package io.tricefal.core.signup

import java.util.*

interface ISignupAdapter {
    fun signup(signup: SignupDomain): SignupDomain
    fun delete(username: String)
    fun findByUsername(username: String): Optional<SignupDomain>
    fun findAll(): List<SignupDomain>
    fun update(signup: SignupDomain): SignupDomain

    fun register(signup: SignupDomain) : Boolean
    fun sendEmail(notification: SignupNotificationDomain) : Boolean
    fun sendSms(notification: SignupNotificationDomain) : Boolean
    fun updateStatus(signup: SignupDomain): SignupDomain
}