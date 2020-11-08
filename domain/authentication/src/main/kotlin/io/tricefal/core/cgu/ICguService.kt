package io.tricefal.core.cgu

import java.util.*

interface ICguService {
    fun findByUsername(username: String): Optional<CguDomain>
    fun save(cgu: CguDomain): CguDomain
}