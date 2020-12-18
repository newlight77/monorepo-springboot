package io.tricefal.core.profile

import org.slf4j.LoggerFactory
import java.util.*

class ProfileService(private var dataAdapter: ProfileDataAdapter) : IProfileService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun findByUsername(username: String): Optional<ProfileDomain> {
        return dataAdapter.findByUsername(username)
    }

    override fun save(profile: ProfileDomain): ProfileDomain {
        return dataAdapter.save(profile)
    }

    override fun updateProfileOnPortraitUploaded(username: String, filename: String): ProfileDomain {
        val profile = ProfileDomain.Builder(username)
                .portraitFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.portraitFilename = filename
                                save(profile)
                            },
                            { save(profile) }
                    )
        } catch (ex: Throwable) {
            logger.error("Failed to update the profile from the portrait uploaded event for user $username")
            throw ProfileUploadException("Failed to update the profile from the portrait uploaded event for user $username", ex)
        }
        return profile
    }

    override fun updateProfileOnResumeUploaded(username: String, filename: String): ProfileDomain {
        val profile = ProfileDomain.Builder(username)
                .resumeFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.resumeFilename = filename
                                save(profile)
                            },
                            { save(profile) }
                    )
        } catch (ex: Throwable) {
            logger.error("Failed to update the profile from the resume uploaded event for user $username")
            throw ProfileUploadException("Failed to update the profile from the resume uploaded event for user $username", ex)
        }
        return profile
    }

    override fun updateProfileOnResumeLinkedinUploaded(username: String, filename: String): ProfileDomain {
        val profile = ProfileDomain.Builder(username)
                .resumeLinkedinFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.resumeLinkedinFilename = filename
                                save(profile)
                            },
                            { save(profile) }
                    )
        } catch (ex: Throwable) {
            logger.error("Failed to update the profile from the linkedin resume uploaded event for user $username")
            throw ProfileUploadException("Failed to update the profile from the linkedin resume uploaded event for user $username", ex)
        }
        return profile
    }

    class ProfileUploadException(private val msg: String, ex: Throwable) : Throwable(ex) {}
}