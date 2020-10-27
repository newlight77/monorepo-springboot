package io.tricefal.core.profile

import java.util.*

interface IProfileService {
    fun findByUsername(username: String): Optional<ProfileDomain>
    fun save(profile: ProfileDomain): ProfileDomain
    fun updateProfileOnPortraitUploaded(username: String, filename: String): ProfileDomain
    fun updateProfileOnResumeUploaded(username: String, filename: String): ProfileDomain
    fun updateProfileOnResumeLinkedinUploaded(username: String, filename: String): ProfileDomain
}