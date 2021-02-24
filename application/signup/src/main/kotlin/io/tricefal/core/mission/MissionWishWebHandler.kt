package io.tricefal.core.mission

import io.tricefal.core.exception.GlobalNotAcceptedException
import io.tricefal.core.exception.GlobalNotFoundException
import io.tricefal.core.metafile.*
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
@PropertySource("classpath:application.yml")
class MissionWishWebHandler(val missionWishService: IMissionWishService,
                            val metafileService: IMetafileService,
                            private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val dataFilesPath = env.getProperty("data.files.path")!!

    fun create(missionWishModel: MissionWishModel): MissionWishModel {
        val domain = fromModel(missionWishModel)
        val result = try {
            missionWishService.create(domain)
        } catch (ex: Throwable) {
            throw MissionWishCreationException("Failed to create a missionWish with username ${missionWishModel.username}", ex)
        }
        return toModel(result)
    }

    fun findByUsername(username: String): MissionWishModel {
        if (username.isEmpty()) throw GlobalNotAcceptedException("username is $username")
        val domain = try {
            this.missionWishService.findByUsername(username)
        } catch (ex: Throwable) {
            throw GlobalNotFoundException("Failed to find a freelance with username $username", ex)
        }
        return toModel(domain)
    }

    fun findAll(): List<MissionWishModel> {
        return missionWishService.findAll().map { missionWishDomain -> toModel(missionWishDomain) }
    }

    fun update(missionWish: MissionWishModel): MissionWishModel {
        val domain = try {
            missionWishService.update(fromModel(missionWish))
        } catch (ex : Throwable) {
            logger.error("Failed to update the missionWish for user ${missionWish.username}")
            throw MissionWishUpdateException("Failed to update the missionWish for user ${missionWish.username}", ex)
        }
        return toModel(domain)
    }

    fun updateResume(username: String, file: MultipartFile): MissionWishModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.CV_MISSION))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            missionWishService.updateOnResumeUploaded(username, metaFile.filename)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the mission specific resume for user $username")
            throw MissionWishUploadException("Failed to upload the mission specific resume for user $username", ex)
        }
        logger.info("successfully upload the mission specific resume for user $username")
        return toModel(result)
    }

    fun resume(username: String): MetafileModel {
        var missionCv = metafileService.findByUsername(username, Representation.CV_MISSION)
        if (missionCv.isEmpty())
            missionCv = metafileService.findByUsername(username, Representation.CV)
        if (missionCv.isEmpty())
            throw GlobalNotFoundException("Failed to find a resume for user $username")
        return missionCv
            .map { toModel(it) }
            .first()
    }

    @Async
    fun updateOnResumeUploaded(username: String, filename: String): MissionWishDomain {
        return try {
            missionWishService.updateOnResumeUploaded(username, filename)
        } catch (ex: Throwable) {
            logger.error("Failed to update mission wish on resume upload for user $username")
            throw MissionWishUploadException("Failed to update mission wish on resume upload for user $username", ex)
        }
    }

    class MissionWishCreationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class MissionWishUpdateException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class MissionWishUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
}