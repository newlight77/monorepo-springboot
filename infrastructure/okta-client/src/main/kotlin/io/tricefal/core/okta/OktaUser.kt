package io.tricefal.core.okta

import io.tricefal.core.signup.SignupDomain

class OktaUser
    private constructor(private val profile: Profile,
              private val credentials: Credentials) {

    class Builder {
        private var firstName: String? = null
        private var lastname: String? = null
        private var email: String? = null
        private var mobilePhone: String? = null
        private var password: String? = null

        fun firstName(firstName: String) =  apply { this.firstName = firstName }
        fun lastname(lastname: String) =  apply { this.lastname = lastname }
        fun email(email: String) =  apply { this.email = email }
        fun mobilePhone(mobilePhone: String) =  apply { this.mobilePhone = mobilePhone }
        fun password(password: String) = apply { this.password = password}

        fun build(): OktaUser {
            val profile = Profile(firstName!!, lastname!!, email!!, email!!, mobilePhone!!)
            val bcryptPassword = BcryptPassword.Builder()
                    .workFactor(10)
                    .password(password!!)
                    .build()
            return OktaUser(profile, Credentials(bcryptPassword))
        }

    }

    private class Profile(val firstName: String,
                  val lastname: String,
                  val email: String,
                  val login: String,
                  val mobilePhone: String)

    private class Credentials(val bcryptPassword: BcryptPassword)
}


fun toOktaUser(domain: SignupDomain): OktaUser {
    return OktaUser.Builder()
            .email(domain.username)
            .password(domain.password!!)
            .firstName(domain.firstname!!)
            .lastname(domain.lastname!!)
            .mobilePhone(domain.phoneNumber!!)
            .build()
}
