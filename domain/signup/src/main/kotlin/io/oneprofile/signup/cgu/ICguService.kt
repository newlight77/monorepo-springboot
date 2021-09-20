package io.oneprofile.signup.cgu

import java.util.*

interface ICguService {
    fun findByUsername(username: String): Optional<CguDomain>
    fun save(username: String, cguVersion: String): CguDomain
}