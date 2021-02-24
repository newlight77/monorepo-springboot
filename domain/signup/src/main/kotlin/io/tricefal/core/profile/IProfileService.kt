package io.tricefal.core.profile

interface IProfileService {
    fun findByUsername(username: String): ProfileDomain
    fun create(profile: ProfileDomain): ProfileDomain
    fun updateProfileOnPortraitUploaded(username: String, filename: String): ProfileDomain
    fun updateProfileOnResumeUploaded(username: String, filename: String): ProfileDomain
    fun updateProfileOnResumeLinkedinUploaded(username: String, filename: String): ProfileDomain
    fun updateStatus(username: String, status: Status): ProfileDomain
    fun updateState(username: String, state: String): ProfileDomain
}