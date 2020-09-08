package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

class ProfileService(private var adapter: IProfileAdapter) : IProfileService {

    override fun portraitUploaded(profile: ProfileDomain, portraitFileDomain: MetafileDomain): ProfileDomain {
        profile.resumeFilename = portraitFileDomain.filename
        adapter.save(profile)
        return profile
    }

    override fun resumeUploaded(profile: ProfileDomain, resumeFileDomain: MetafileDomain): ProfileDomain {
        profile.resumeFilename = resumeFileDomain.filename
        adapter.save(profile)
        return profile
    }

    override fun refUploaded(profile: ProfileDomain, refFileDomain: MetafileDomain): ProfileDomain {
        profile.resumeFilename = refFileDomain.filename
        adapter.save(profile)
        return profile
    }

    override fun findByUsername(username: String): Optional<ProfileDomain> {
        return adapter.findByUsername(username)
    }
}