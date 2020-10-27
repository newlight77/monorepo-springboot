package io.tricefal.core.profile

import io.tricefal.core.exception.NotAcceptedException
import io.tricefal.core.exception.NotFoundException
import io.tricefal.core.metafile.*
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception


@Service
@PropertySource("classpath:application.yml")
class ProfileWebHandler(val profileService: IProfileService,
                        val metafileService: IMetafileService,
                        private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val dataFilesPath = env.getProperty("data.files.path")!!

    fun find(username: String): ProfileModel {
         return toModel(findProfile(username))
    }

    fun portrait(username: String): MetafileModel {
        return metafileService.findByUsername(username)
                .map { toModel(it) }
                .first { it.representation == Representation.PORTRAIT }
    }

    fun resume(username: String): MetafileModel {
        return metafileService.findByUsername(username)
                .map { toModel(it) }
                .first { it.representation == Representation.CV }
    }

    fun resumeLinkedIn(username: String): MetafileModel {
        return metafileService.findByUsername(username)
                .map { toModel(it) }
                .first { it.representation == Representation.CV_LINKEDIN }
    }

    fun uploadPortrait(username: String, file: MultipartFile): ProfileModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.PORTRAIT))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            this.profileService.updateProfileOnPortraitUploaded(username, metaFile.filename)
        } catch(ex: Exception) {
            logger.error("Failed to update the profile portrait for username $username")
            throw ProfileUploadException("Failed to update the profile portrait for username $username")
        }
        logger.info("successfully upload the portrait for user $username")
        return toModel(result)
    }

    fun uploadResume(username: String, file: MultipartFile): ProfileModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.CV))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            this.profileService.updateProfileOnResumeUploaded(username, metaFile.filename)
        } catch(ex: Exception) {
            logger.error("Failed to update the profile resume for username $username")
            throw ProfileUploadException("Failed to update the profile resume for username $username")
        }
        logger.info("successfully upload the resume for user $username")
        return toModel(result)
    }

    fun uploadResumeLinkedin(username: String, file: MultipartFile): ProfileModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.CV_LINKEDIN))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            this.profileService.updateProfileOnResumeLinkedinUploaded(username, metaFile.filename)
        } catch(ex: Exception) {
            logger.error("Failed to update the profile linkedin resume for username $username")
            throw ProfileUploadException("Failed to update the profile cv linkedin resume for username $username")
        }

        logger.info("successfully upload the linkedin resume for user $username")
        return toModel(result)
    }

    private fun findProfile(username: String): ProfileDomain {
        if (username.isEmpty()) throw NotAcceptedException("username is $username")
        val profile = this.profileService.findByUsername(username)
                .orElseThrow { NotFoundException("username $username not found") }
        return profile
    }

}

class ProfileUploadException(private val msg: String) : Throwable(msg) {}