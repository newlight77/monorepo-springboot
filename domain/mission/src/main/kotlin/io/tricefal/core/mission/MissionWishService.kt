package io.tricefal.core.mission

import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

class MissionWishService(private var adapter: IMissionWishAdapter) : IMissionWishService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(missionWish: MissionWishDomain): MissionWishDomain {
        val result = adapter.findByUsername(missionWish.username)
        return if (result.isPresent) adapter.update(missionWish)
        else adapter.create(missionWish)
    }

    override fun findByUsername(username: String): Optional<MissionWishDomain> {
        return adapter.findByUsername(username)
    }

    override fun findAll(): List<MissionWishDomain> {
        return adapter.findAll()
    }

    override fun updated(missionWish: MissionWishDomain): MissionWishDomain {
        missionWish.lastDate = Instant.now()
        adapter.update(missionWish)
        return missionWish
    }

    override fun updateOnResumeUploaded(username: String, filename: String): MissionWishDomain {
        val missionWish = MissionWishDomain.Builder(username)
                .resumeFilename(filename)
                .build()
        try {
            this.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.resumeFilename = filename
                                create(missionWish)
                            },
                            { create(missionWish) }
                    )
        } catch (ex: Exception) {
            logger.error("Failed to update the mission wish from the resume uploaded event for user $username")
            throw MissionResumeUploadException("Failed to mission wish the profile from the resume uploaded event for user $username")
        }
        return missionWish
    }

}

class NotFoundException(val s: String) : Throwable()
class UsernameNotFoundException(val s: String) : Throwable()
class MissionResumeUploadException(val s: String) : Throwable()