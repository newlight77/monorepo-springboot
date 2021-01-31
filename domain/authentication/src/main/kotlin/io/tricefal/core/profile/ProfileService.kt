package io.tricefal.core.profile

import org.slf4j.LoggerFactory

class ProfileService(private var dataAdapter: ProfileDataAdapter) : IProfileService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun findByUsername(username: String): ProfileDomain {
        val result = dataAdapter.findByUsername(username)
        return if (result.isPresent) {
            result.get()
        } else throw NotFoundException("Failed to find a freelance for user $username")
    }

    override fun save(profile: ProfileDomain): ProfileDomain {
        dataAdapter.findByUsername(profile.username).ifPresent {
            throw DuplicateException("a profile with username ${profile.username} already exists")
        }
        return dataAdapter.create(profile)
    }

    override fun initProfile(username: String, profile: ProfileDomain): ProfileDomain {
        var newprofile = ProfileDomain.Builder(username).build()
        try {
            this.dataAdapter.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.status = profile.status
                        newprofile = dataAdapter.update(it)
                    },
                    {
                        newprofile = dataAdapter.create(profile)
                    }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the profile from the status update event for user $username")
            throw ProfileUploadException("Failed to update the profile from the status update event for user $username", ex)
        }
        return newprofile
    }

    override fun updateState(username: String, state: String): ProfileDomain {
        var profile = ProfileDomain.Builder(username)
            .state(ProfileStateDomain.Builder(username).build())
            .build()
        profile.state?.updateState(state)
        try {
            this.dataAdapter.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.state?.updateState(state)
                        profile = dataAdapter.update(it)
                    },
                    { profile = dataAdapter.create(profile) }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the profile from the signup state update event for user $username")
            throw ProfileUploadException("Failed to update the profile from the signup state update event for user $username", ex)
        }
        return profile
    }

    override fun updateProfileOnPortraitUploaded(username: String, filename: String): ProfileDomain {
        var profile = ProfileDomain.Builder(username)
            .state(ProfileStateDomain.Builder(username).portraitUploaded(true).build())
            .portraitFilename(filename)
            .build()
        try {
            this.dataAdapter.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.portraitFilename = filename
                        it.state?.portraitUploaded = true
                        profile = dataAdapter.update(it)
                    },
                    { profile = dataAdapter.create(profile) }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the profile from the portrait uploaded event for user $username")
            throw ProfileUploadException("Failed to update the profile from the portrait uploaded event for user $username", ex)
        }
        return profile
    }

    override fun updateProfileOnResumeUploaded(username: String, filename: String): ProfileDomain {
        var profile = ProfileDomain.Builder(username)
            .state(ProfileStateDomain.Builder(username).resumeUploaded(true).build())
            .resumeFilename(filename)
            .build()
        try {
            this.dataAdapter.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.resumeFilename = filename
                        it.state?.resumeUploaded = true
                        profile = dataAdapter.update(it)
                    },
                    { profile = dataAdapter.create(profile) }
                )
            this.dataAdapter.resumeUploaded(username, filename)
        } catch (ex: Throwable) {
            logger.error("Failed to update the profile from the resume uploaded event for user $username")
            throw ProfileUploadException("Failed to update the profile from the resume uploaded event for user $username", ex)
        }
        return profile
    }

    override fun updateProfileOnResumeLinkedinUploaded(username: String, filename: String): ProfileDomain {
        var profile = ProfileDomain.Builder(username)
            .state(ProfileStateDomain.Builder(username).resumeLinkedinUploaded(true).build())
            .resumeLinkedinFilename(filename)
            .build()
        try {
            this.dataAdapter.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.resumeLinkedinFilename = filename
                        it.state?.resumeLinkedinUploaded = true
                        profile = dataAdapter.update(it)
                    },
                    { profile = dataAdapter.create(profile) }
                )
            this.dataAdapter.resumeLinkedinUploaded(username, filename)
        } catch (ex: Throwable) {
            logger.error("Failed to update the profile from the linkedin resume uploaded event for user $username")
            throw ProfileUploadException("Failed to update the profile from the linkedin resume uploaded event for user $username", ex)
        }
        return profile
    }

    class ProfileUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class NotFoundException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class DuplicateException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }

}