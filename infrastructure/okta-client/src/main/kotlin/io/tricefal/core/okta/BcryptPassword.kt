package io.tricefal.core.okta

import org.mindrot.jbcrypt.BCrypt

class BcryptPassword
    private constructor(private val algorithm: Algorithm = Algorithm.BCRYPT,
                        private val workFactor: Int = 10,
                        private val value: String) {
    class Builder {
        private var workFactor: Int = 10
        private lateinit var password: String

        fun workFactor(workFactor: Int) =  apply { this.workFactor = workFactor }
        fun password(password: String) =  apply { this.password = password }

        fun build(): BcryptPassword {
            val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(workFactor))
            return BcryptPassword(Algorithm.BCRYPT, workFactor, hashedPassword)
        }
    }

    private enum class Algorithm {
        BCRYPT
    }
}

