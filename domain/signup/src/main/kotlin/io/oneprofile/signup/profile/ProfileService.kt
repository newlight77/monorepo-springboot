package io.oneprofile.signup.profile

import org.slf4j.LoggerFactory

class ProfileService(private var dataAdapter: ProfileDataAdapter) : IProfileService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun findByUsername(username: String): ProfileDomain {
        val result = dataAdapter.findByUsername(username)
        return if (result.isPresent) {
            result.get()
        } else throw NotFoundException("Failed to find a freelance for user $username")
    }

    override fun create(profile: ProfileDomain): ProfileDomain {
        dataAdapter.findByUsername(profile.username).ifPresent {
            throw DuplicateException("a profile with username ${profile.username} already exists")
        }
        if (profile.state == null) profile.state = ProfileStateDomain.Builder(profile.username).saved(true).build()
        return dataAdapter.create(profile)
    }

    override fun updateStatus(username: String, status: Status): ProfileDomain {
        var profile = ProfileDomain.Builder(username)
            .state(ProfileStateDomain.Builder(username).build())
            .build()
        try {
            this.dataAdapter.findByUsername(username)
                .ifPresentOrElse(
                    {
                        it.status = status
                        it.state?.statusUpdated = true
                        profile = dataAdapter.update(it)
                    },
                    { profile = dataAdapter.create(profile) }
                )
        } catch (ex: Throwable) {
            logger.error("Failed to update the profile from the signup state update event for user $username", ex)
            throw ProfileUploadException("Failed to update the profile from the signup state update event for user $username", ex)
        }
        return profile
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
            logger.error("Failed to update the profile from the signup state update event for user $username", ex)
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
            logger.error("Failed to update the profile from the portrait uploaded event for user $username", ex)
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
        } catch (ex: Throwable) {
            logger.error("Failed to update the profile from the resume uploaded event for user $username", ex)
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
        } catch (ex: Throwable) {
            logger.error("Failed to update the profile from the linkedin resume uploaded event for user $username", ex)
            throw ProfileUploadException("Failed to update the profile from the linkedin resume uploaded event for user $username", ex)
        }
        return profile
    }

    override fun updateProfilePortrait(username: String, filename: String): ProfileDomain {
        val profile = updateProfileOnPortraitUploaded(username, filename)
        this.dataAdapter.resumeUploaded(username, filename)
        return profile
    }

    override fun updateProfileResume(username: String, filename: String): ProfileDomain {
        val profile = updateProfileOnResumeUploaded(username, filename)
        this.dataAdapter.resumeUploaded(username, filename)
        return profile
    }

    override fun updateProfileResumeLinkedin(username: String, filename: String): ProfileDomain {
        val profile = updateProfileOnResumeLinkedinUploaded(username, filename)
        this.dataAdapter.resumeLinkedinUploaded(username, filename)
        return profile
    }

    class ProfileUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class ProfileUpdateException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class NotFoundException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class DuplicateException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }

}