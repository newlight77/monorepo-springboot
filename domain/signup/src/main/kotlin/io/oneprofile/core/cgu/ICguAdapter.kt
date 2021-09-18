package io.oneprofile.core.cgu

import java.util.*

interface ICguAdapter {
    fun save(cgu: CguDomain): CguDomain
    fun acceptCgu(cgu: CguDomain): CguDomain
    fun findByUsername(username: String): Optional<CguDomain>
}