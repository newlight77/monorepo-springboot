package io.tricefal.core.mission

import org.slf4j.LoggerFactory
import java.time.Instant

class MissionWishService(private var adapter: IMissionWishAdapter) : IMissionWishService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun create(missionWish: MissionWishDomain): MissionWishDomain {
        adapter.findByUsername(missionWish.username).ifPresent {
            throw UsernameNotFoundException("a missionWish with username ${missionWish.username} is already taken")
        }

        return adapter.create(missionWish)
    }

    override fun findByUsername(username: String): MissionWishDomain {
        if (username.isEmpty()) throw UsernameNotFoundException("username is $username")
        return adapter.findByUsername(username)
                .orElseThrow { NotFoundException("resource not found for username $username") }
    }

    override fun findAll(): List<MissionWishDomain> {
        return adapter.findAll()
    }

    override fun updated(missionWish: MissionWishDomain): MissionWishDomain {
        missionWish.lastDate = Instant.now()
        adapter.update(missionWish)
        return missionWish
    }

}

class NotFoundException(val s: String) : Throwable()
class UsernameNotFoundException(val s: String) : Throwable()
