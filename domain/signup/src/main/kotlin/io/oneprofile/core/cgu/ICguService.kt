package io.oneprofile.core.cgu

import java.util.*

interface ICguService {
    fun findByUsername(username: String): Optional<CguDomain>
    fun save(username: String, cguVersion: String): CguDomain
}