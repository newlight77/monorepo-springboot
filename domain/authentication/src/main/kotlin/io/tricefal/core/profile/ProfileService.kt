package io.tricefal.core.profile

import io.tricefal.core.metafile.MetafileDomain
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.util.*

class ProfileService(private var adapter: IProfileAdapter) : IProfileService {

    private val logger = LoggerFactory.getLogger(this::class.java)

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

    override fun save(profile: ProfileDomain): ProfileDomain {
        return adapter.save(profile)
    }

    override fun updateProfileOnPortraitUploaded(username: String, filename: String): Boolean {
        val profile = ProfileDomain.Builder(username)
                .portraitFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.portraitFilename = profile.portraitFilename
                                save(profile)
                            },
                            { save(profile) }
                    )
        } catch (ex: Exception) {
            logger.error("Failed to update the profile from the portrait uploaded event for user $username")
            throw ProfileUploadException("Failed to update the profile from the portrait uploaded event for user $username")
        }
        return true
    }

    override fun updateProfileOnResumeUploaded(username: String, filename: String): Boolean {
        val profile = ProfileDomain.Builder(username)
                .resumeFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.resumeFilename = profile.resumeFilename
                                save(profile)
                            },
                            { save(profile) }
                    )
        } catch (ex: Exception) {
            logger.error("Failed to update the profile from the portrait uploaded event for user $username")
            throw ProfileUploadException("Failed to update the profile from the portrait uploaded event for user $username")
        }
        return true
    }

    override fun updateProfileOnRefUploaded(username: String, filename: String): Boolean {
        val profile = ProfileDomain.Builder(username)
                .refFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.refFilename = profile.refFilename
                                save(profile)
                            },
                            { save(profile) }
                    )
        } catch (ex: Exception) {
            logger.error("Failed to update the profile from the portrait uploaded event for user $username")
            throw ProfileUploadException("Failed to update the profile from the portrait uploaded event for user $username")
        }
        return true
    }

    class ProfileUploadException(private val msg: String) : Throwable(msg) {}
}