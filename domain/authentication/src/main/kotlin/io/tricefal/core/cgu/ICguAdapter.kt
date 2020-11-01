package io.tricefal.core.cgu

import java.util.*

interface ICguAdapter {
    fun save(profile: CguDomain): CguDomain
    fun acceptCgu(profile: CguDomain): CguDomain
    fun findByUsername(username: String): Optional<CguDomain>
}