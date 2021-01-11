package io.tricefal.core.profile

interface IProfileService {
    fun findByUsername(username: String): ProfileDomain
    fun save(profile: ProfileDomain): ProfileDomain
    fun updateProfileOnPortraitUploaded(username: String, filename: String): ProfileDomain
    fun updateProfileOnResumeUploaded(username: String, filename: String): ProfileDomain
    fun updateProfileOnResumeLinkedinUploaded(username: String, filename: String): ProfileDomain
    fun updateStatus(username: String, status: String): ProfileDomain
    fun updateState(username: String, state: String): ProfileDomain
}