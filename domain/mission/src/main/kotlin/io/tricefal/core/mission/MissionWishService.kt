package io.tricefal.core.mission

import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

class MissionWishService(private var dataAdapter: MissionWishDataAdapter) : IMissionWishService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(missionWish: MissionWishDomain): MissionWishDomain {
        val result = dataAdapter.findByUsername(missionWish.username)
        return if (result.isPresent) dataAdapter.update(missionWish)
        else dataAdapter.create(missionWish)
    }

    override fun findByUsername(username: String): Optional<MissionWishDomain> {
        return dataAdapter.findByUsername(username)
    }

    override fun findAll(): List<MissionWishDomain> {
        return dataAdapter.findAll()
    }

    override fun updated(missionWish: MissionWishDomain): MissionWishDomain {
        missionWish.lastDate = Instant.now()
        dataAdapter.update(missionWish)
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
        } catch (ex: Throwable) {
            logger.error("Failed to update the mission wish from the resume uploaded event for user $username")
            throw MissionResumeUploadException("Failed to mission wish the profile from the resume uploaded event for user $username", ex)
        }
        return missionWish
    }

}

class MissionResumeUploadException(val s: String, ex: Throwable) : Throwable(ex)