package io.oneprofile.signup.cgu

import java.util.*

interface ICguAdapter {
    fun save(cgu: CguDomain): CguDomain
    fun acceptCgu(cgu: CguDomain): CguDomain
    fun findByUsername(username: String): Optional<CguDomain>
}