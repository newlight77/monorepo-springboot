package io.tricefal.core.okta

import io.tricefal.core.signup.SignupDomain

class OktaUser(val profile: Profile,
               val credentials: Credentials) {

    class Builder {
        private var firstName: String? = null
        private var lastName: String? = null
        private var email: String? = null
        private var mobilePhone: String? = null
        private var password: String? = null

        fun firstName(firstName: String) =  apply { this.firstName = firstName }
        fun lastName(lastName: String) =  apply { this.lastName = lastName }
        fun email(email: String) =  apply { this.email = email }
        fun mobilePhone(mobilePhone: String) =  apply { this.mobilePhone = mobilePhone }
        fun password(password: String) = apply { this.password = password}

        fun build(): OktaUser {
            val profile = Profile(firstName!!, lastName!!, email!!, email!!, mobilePhone!!)
            val passwordHash = PasswordHash.Builder()
                    .workFactor(10)
                    .password(password!!)
                    .build()
            return OktaUser(profile, Credentials(Password(passwordHash)))
        }

    }

    class Profile(val firstName: String,
                  val lastName: String,
                  val email: String,
                  val login: String,
                  val mobilePhone: String)

    class Credentials(val password: Password)

    class Password(val hash: PasswordHash)
}


fun toOktaUser(domain: SignupDomain): OktaUser {
    return OktaUser.Builder()
            .email(domain.username)
            .password(domain.password!!)
            .firstName(domain.firstname!!)
            .lastName(domain.lastname!!)
            .mobilePhone(domain.phoneNumber!!)
            .build()
}
