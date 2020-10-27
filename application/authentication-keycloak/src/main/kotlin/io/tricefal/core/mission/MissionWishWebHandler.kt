package io.tricefal.core.mission

import io.tricefal.core.exception.NotAcceptedException
import io.tricefal.core.metafile.IMetafileService
import io.tricefal.core.metafile.Representation
import io.tricefal.core.metafile.fromModel
import io.tricefal.core.metafile.toMetafile
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception
import java.util.*


@Service
@PropertySource("classpath:application.yml")
class MissionWishWebHandler(val missionWishService: IMissionWishService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun create(missionWishModel: MissionWishModel): MissionWishModel {
        val domain = fromModel(missionWishModel)
        val result = try {
            missionWishService.create(domain)
        } catch (ex: Exception) {
            throw MissionWishCreationException("Failed to create a missionWish with username ${missionWishModel.username}")
        }
        return toModel(result)
    }

    fun findByUsername(username: String): MissionWishModel {
        if (username.isEmpty()) throw NotAcceptedException("username is $username")
        return toModel(missionWishService.findByUsername(username))
    }

    fun findAll(): List<MissionWishModel> {
        return missionWishService.findAll().map { missionWishDomain -> toModel(missionWishDomain) }
    }

    fun update(missionWish: MissionWishModel): MissionWishModel {
        val domain = try {
            missionWishService.updated(fromModel(missionWish))
        } catch (ex : Exception) {
            logger.error("Failed to update the missionWish for user ${missionWish.username}")
            throw MissionWishUpdateException("Failed to update the missionWish for user ${missionWish.username}")
        }
        return toModel(domain)
    }

    class MissionWishCreationException(private val msg: String) : Throwable(msg) {}
    class MissionWishUpdateException(private val msg: String) : Throwable(msg) {}
}