package io.tricefal.core.profile

import java.util.*

interface ProfileDataAdapter {
    fun create(profile: ProfileDomain): ProfileDomain
    fun findByUsername(username: String): Optional<ProfileDomain>
    fun update(profile: ProfileDomain): ProfileDomain
    fun resumeUploaded(username: String, filename: String)
    fun resumeLinkedinUploaded(username: String, filename: String)
}