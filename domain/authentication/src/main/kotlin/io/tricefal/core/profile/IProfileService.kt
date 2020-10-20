package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

interface IProfileService {
    fun portraitUploaded(profile: ProfileDomain, fileDomain: MetafileDomain): ProfileDomain
    fun resumeUploaded(profile: ProfileDomain, fileDomain: MetafileDomain): ProfileDomain
    fun refUploaded(profile: ProfileDomain, fileDomain: MetafileDomain): ProfileDomain
    fun findByUsername(username: String): Optional<ProfileDomain>
}