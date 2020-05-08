package io.tricefal.core.signup

import java.util.*

interface ISignupRepository {
    fun save(signup: SignupDomain)
    fun findByUsername(username: String): Optional<SignupDomain>
    fun update(signup: SignupDomain)
}