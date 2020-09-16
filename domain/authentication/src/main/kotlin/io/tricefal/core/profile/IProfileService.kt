package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

interface IProfileService {
    fun portraitUploaded(profile: ProfileDomain, portraitFileDomain: MetafileDomain): ProfileDomain
    fun resumeUploaded(profile: ProfileDomain, resumeFileDomain: MetafileDomain): ProfileDomain
    fun refUploaded(profile: ProfileDomain, refFileDomain: MetafileDomain): ProfileDomain
    fun findByUsername(username: String): Optional<ProfileDomain>
}