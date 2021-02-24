package io.tricefal.core.mission

import org.slf4j.LoggerFactory
import java.time.Instant

class MissionWishService(private var dataAdapter: MissionWishDataAdapter) : IMissionWishService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(missionWish: MissionWishDomain): MissionWishDomain {
        val result = dataAdapter.findByUsername(missionWish.username)
        return if (result.isPresent) dataAdapter.update(missionWish)
        else dataAdapter.create(missionWish)
    }

    override fun findByUsername(username: String): MissionWishDomain {
        val result = dataAdapter.findByUsername(username)
        return if (result.isPresent) {
            result.get()
        } else throw NotFoundException("Failed to find a freelance for user $username")
    }

    override fun findAll(): List<MissionWishDomain> {
        return dataAdapter.findAll()
    }

    override fun update(missionWish: MissionWishDomain): MissionWishDomain {
        missionWish.lastDate = Instant.now()
        dataAdapter.update(missionWish)
        return missionWish
    }

    override fun updateOnResumeUploaded(username: String, filename: String): MissionWishDomain {
        val missionWish = MissionWishDomain.Builder(username)
            .resumeFilename(filename)
            .build()
        try {
            this.dataAdapter.findByUsername(username)
                    .ifPresentOrElse(
                            {
                                it.resumeFilename = filename
                                it.lastDate = it.lastDate ?: Instant.now()
                                update(it)
                            },
                            { create(missionWish) }
                    )
            dataAdapter.updateOnResumeUploaded(username, filename)
        } catch (ex: Throwable) {
            logger.error("Failed to update the mission wish from the resume uploaded event for user $username")
            throw MissionResumeUploadException("Failed to mission wish the profile from the resume uploaded event for user $username", ex)
        }
        return missionWish
    }

}

class MissionResumeUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
class NotFoundException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}