package io.tricefal.core.keycloak

import io.tricefal.core.signup.SignupDomain
import java.time.Instant

class KeycloakNewUser(
        val email: String,
        val username: String,
//        val password: String,
        val firstName: String,
        val lastName: String,
        val enabled: Boolean = true,
        val emailVerified: Boolean = true,
//        val groups: String? = null,
//        val realmRoles: String? = null,
        val credentials: Credentials? = null) {

    class Builder {
        private var username: String? = null
//        private var password: String? = null
        private var firstName: String? = null
        private var lastName: String? = null
//        private var groups: String? = null
//        private var realmRoles: String? = null

        fun username(username: String) =  apply { this.username = username }
//        fun password(password: String) = apply { this.password = password}
        fun firstName(firstName: String) =  apply { this.firstName = firstName }
        fun lastName(lastName: String) =  apply { this.lastName = lastName }
//        fun groups(groups: String) =  apply { this.groups = groups }
//        fun realmRoles(realmRoles: String) =  apply { this.realmRoles = realmRoles }

        fun build(): KeycloakNewUser {
//            val workFactor: Int = 10
//            val salt = BCrypt.gensalt(workFactor)
//            val hashedPassword = BCrypt.hashpw(password, salt)
//            val credentials = Credentials("BCRYPT",
//                    Instant.now(),
//                    salt.substring(salt.lastIndexOf("$") + 1),
//                    hashedPassword.substring(salt.lastIndex + 1),
//                    "")
            return KeycloakNewUser(
                    username = this.username!!,
                    email = this.username!!,
//                    password = this.password!!,
                    firstName = this.firstName!!,
                    lastName = this.lastName!!,
                    enabled = true,
                    emailVerified = true
//                    groups =this.groups!!,
//                    realmRoles= this.realmRoles!!,
//                    credentials = credentials
            )
        }

    }

    class Credentials(
            val algorithm: String,
            val createdDate: Instant,
//            val digits: Int,
//            val counter: Int,
            val salt: String,
            val hashedSaltedValue: String,
            val value: String
    )
}


fun toKeycloakNewUser(domain: SignupDomain): KeycloakNewUser {
    return KeycloakNewUser.Builder()
            .username(domain.username)
//            .password(domain.password!!)
            .firstName(domain.firstname!!)
            .lastName(domain.lastname!!)
//            .groups("")
//            .realmRoles("")
            .build()
}
