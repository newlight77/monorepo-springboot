package io.tricefal.core.okta

import org.mindrot.jbcrypt.BCrypt

class PasswordHash
    private constructor(private val algorithm: String = Algorithm.BCRYPT.name,
                        private val workFactor: Int = 10,
                        val salt: String,
                        val value: String) {
    class Builder {
        private var workFactor: Int = 10
        private lateinit var password: String

        fun workFactor(workFactor: Int) =  apply { this.workFactor = workFactor }
        fun password(password: String) =  apply { this.password = password }

        fun build(): PasswordHash {
            // salt : "$2a$10$37H.sl/DeiiHjXQwl3d4zO"
            // hash : "$2a$10$37H.sl/DeiiHjXQwl3d4zOFT3AnuJp4jxsZceOJw.HAU6g5KJHJbO"
            // four components of a Bcrypt MCF hash: $<id>$<cost>$<salt><digest>
            // 1. $<id>$ can be represented in 3 bits.
            // 2. <cost>$, 04-31, can be represented in 5 bits. Put these together for 1 byte.
            // 3. The 22-character salt is a (non-standard) base-64 representation of 128 bits. Base-64 decoding yields 16 bytes.
            // 4. The 31-character hash digest can be base-64 decoded to 23 bytes.
            // 5. Put it all together for 40 bytes: 1 + 16 + 23
            val salt = BCrypt.gensalt(workFactor)
            val hashedPassword = BCrypt.hashpw(password, salt)
            return PasswordHash(
                    Algorithm.BCRYPT.name,
                    workFactor,
                    salt.substring(salt.lastIndexOf("$") + 1), // okta expects only 22-character salt
                    hashedPassword.substring(salt.lastIndex + 1)) // okta expects only 31-character hash
        }
    }

    private enum class Algorithm {
        BCRYPT
    }
}

