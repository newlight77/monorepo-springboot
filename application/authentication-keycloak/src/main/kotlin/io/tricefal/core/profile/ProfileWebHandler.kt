package io.tricefal.core.profile

import io.tricefal.core.exception.GlobalNotAcceptedException
import io.tricefal.core.exception.GlobalNotFoundException
import io.tricefal.core.metafile.*
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
@PropertySource("classpath:application.yml")
class ProfileWebHandler(val profileService: IProfileService,
                        val metafileService: IMetafileService,
                        private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val dataFilesPath = env.getProperty("data.files.path")!!

    fun initProfile(username: String, profile: ProfileDomain) {
        try {
            this.profileService.create(profile)
            //this.profileService.initProfile(username, profile)
        } catch(ex: Throwable) {
            logger.error("Failed to update the profile status for username $username")
            throw ProfileUploadException("Failed to update the profile status for username $username", ex)
        }
    }

    fun findByUsername(username: String): ProfileModel {
        if (username.isEmpty()) throw GlobalNotAcceptedException("username is $username")
        val domain = try {
            this.profileService.findByUsername(username)
        } catch (ex: Throwable) {
            throw GlobalNotFoundException("Failed to find a freelance with username $username", ex)
        }
        return toModel(domain)
    }

    fun portrait(username: String): MetafileModel {
        return metafileService.findByUsername(username, Representation.PORTRAIT)
                .map { toModel(it) }
                .first()
    }

    fun resume(username: String): MetafileModel {
        return metafileService.findByUsername(username, Representation.CV)
                .map { toModel(it) }
                .first()
    }

    fun resumeLinkedIn(username: String): MetafileModel {
        return metafileService.findByUsername(username, Representation.CV_LINKEDIN)
                .map { toModel(it) }
                .first()
    }

    fun uploadPortrait(username: String, file: MultipartFile): ProfileModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.PORTRAIT))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            this.profileService.updateProfileOnPortraitUploaded(username, metaFile.filename)
        } catch(ex: Throwable) {
            logger.error("Failed to update the profile portrait for username $username")
            throw ProfileUploadException("Failed to update the profile portrait for username $username", ex)
        }
        logger.info("successfully upload the portrait for user $username")
        return toModel(result)
    }

    fun uploadResume(username: String, file: MultipartFile): ProfileModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.CV))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            this.profileService.updateProfileOnResumeUploaded(username, metaFile.filename)
        } catch(ex: Throwable) {
            logger.error("Failed to update the profile resume for username $username")
            throw ProfileUploadException("Failed to update the profile resume for username $username", ex)
        }
        logger.info("successfully upload the resume for user $username")
        return toModel(result)
    }

    fun uploadResumeLinkedin(username: String, file: MultipartFile): ProfileModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.CV_LINKEDIN))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            this.profileService.updateProfileOnResumeLinkedinUploaded(username, metaFile.filename)
        } catch(ex: Throwable) {
            logger.error("Failed to update the profile linkedin resume for username $username")
            throw ProfileUploadException("Failed to update the profile cv linkedin resume for username $username", ex)
        }

        logger.info("successfully upload the linkedin resume for user $username")
        return toModel(result)
    }

    fun updateState(username: String, state: String) {
        try {
            this.profileService.updateState(username, state)
        } catch(ex: Throwable) {
            logger.error("Failed to update the profile state for username $username")
            throw ProfileUploadException("Failed to update the profile state for username $username", ex)
        }
    }

    fun updateProfileOnResumeUploaded(username: String, filename: String) {
        try {
            this.profileService.updateProfileOnResumeUploaded(username, filename)
        } catch(ex: Throwable) {
            logger.error("Failed to update the profile resume filename for username $username")
            throw ProfileUploadException("Failed to update the profile resume filename for username $username", ex)
        }
    }

    fun updateProfileOnResumeLinkedinUploaded(username: String, filename: String) {
        try {
            this.profileService.updateProfileOnResumeLinkedinUploaded(username, filename)
        } catch(ex: Throwable) {
            logger.error("Failed to update the profile resume linkedin filename for username $username")
            throw ProfileUploadException("Failed to update the profile resume linkedin filename for username $username", ex)
        }
    }

}

class ProfileUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}