package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

class ProfileService(private var adapter: IProfileAdapter) : IProfileService {

    override fun portraitUploaded(profile: ProfileDomain, fileDomain: MetafileDomain): ProfileDomain {
        profile.resumeFilename = fileDomain.filename
        adapter.save(profile)
        return profile
    }

    override fun resumeUploaded(profile: ProfileDomain, fileDomain: MetafileDomain): ProfileDomain {
        profile.resumeFilename = fileDomain.filename
        adapter.save(profile)
        return profile
    }

    override fun refUploaded(profile: ProfileDomain, fileDomain: MetafileDomain): ProfileDomain {
        profile.resumeFilename = fileDomain.filename
        adapter.save(profile)
        return profile
    }

    override fun findByUsername(username: String): Optional<ProfileDomain> {
        return adapter.findByUsername(username)
    }
}