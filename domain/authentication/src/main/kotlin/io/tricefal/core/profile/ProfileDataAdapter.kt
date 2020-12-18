package io.tricefal.core.profile

import java.util.*

interface ProfileDataAdapter {
    fun save(profile: ProfileDomain): ProfileDomain
    fun findByUsername(username: String): Optional<ProfileDomain>
}