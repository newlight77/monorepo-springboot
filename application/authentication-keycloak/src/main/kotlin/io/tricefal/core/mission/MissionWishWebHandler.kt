package io.tricefal.core.mission

import io.tricefal.core.exception.NotAcceptedException
import io.tricefal.core.exception.NotFoundException
import io.tricefal.core.metafile.*
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
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
        if (username.isEmpty()) throw NotAcceptedException("username is $username")
        return toModel(find(username))
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
            missionWishService.updateOnResumeUploaded(username, metaFile)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the mission specific resume for user $username")
            throw MissionWishUploadException("Failed to upload the mission specific resume for user $username", ex)
        }
        logger.info("successfully upload the mission specific resume for user $username")
        return toModel(result)
    }

    fun resume(username: String): MetafileModel {
        return metafileService.findByUsername(username)
                .map { toModel(it) }
                .first { it.representation == Representation.CV_MISSION }
    }

    private fun find(username: String): MissionWishDomain {
        if (username.isEmpty()) throw NotAcceptedException("username is $username")
        val missionWish = this.missionWishService.findByUsername(username)
                .orElseThrow { NotFoundException("username $username not found") }
        return missionWish
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