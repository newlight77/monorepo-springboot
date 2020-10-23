package io.tricefal.core.profile

import java.util.*

interface IProfileAdapter {
    fun save(profile: ProfileDomain): ProfileDomain
    fun findByUsername(username: String): Optional<ProfileDomain>
}