package io.tricefal.core.mission

import java.util.*

interface IMissionWishService {
    fun create(missionWish: MissionWishDomain) : MissionWishDomain
    fun findByUsername(username: String): Optional<MissionWishDomain>
    fun findAll(): List<MissionWishDomain>
    fun updated(missionWish: MissionWishDomain): MissionWishDomain
    fun updateOnResumeUploaded(username: String, filename: String): MissionWishDomain
}