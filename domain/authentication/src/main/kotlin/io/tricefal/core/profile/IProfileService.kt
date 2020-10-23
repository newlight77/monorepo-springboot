package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

interface IProfileService {
    fun portraitUploaded(profile: ProfileDomain, fileDomain: MetafileDomain): ProfileDomain
    fun resumeUploaded(profile: ProfileDomain, fileDomain: MetafileDomain): ProfileDomain
    fun refUploaded(profile: ProfileDomain, fileDomain: MetafileDomain): ProfileDomain
    fun findByUsername(username: String): Optional<ProfileDomain>
    fun save(profile: ProfileDomain): ProfileDomain
    fun updateProfileOnPortraitUploaded(username: String, filename: String): Boolean
    fun updateProfileOnResumeUploaded(username: String, filename: String): Boolean
    fun updateProfileOnRefUploaded(username: String, filename: String): Boolean
}