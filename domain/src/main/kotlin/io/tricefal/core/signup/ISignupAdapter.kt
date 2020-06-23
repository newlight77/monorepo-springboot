package io.tricefal.core.signup

import java.util.*

interface ISignupAdapter {
    fun signup(signup: SignupDomain): SignupDomain
    fun findByUsername(username: String): Optional<SignupDomain>
    fun update(signup: SignupDomain): SignupDomain

    fun oktaRegister(signup: SignupDomain) : Boolean
    fun sendEmail(notification: SignupNotificationDomain) : Boolean
    fun sendSms(notification: SignupNotificationDomain) : Boolean
}