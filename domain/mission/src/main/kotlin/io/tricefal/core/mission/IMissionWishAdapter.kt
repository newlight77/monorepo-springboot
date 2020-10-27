package io.tricefal.core.mission

import java.util.*

interface IMissionWishAdapter {
    fun create(missionWish: MissionWishDomain): MissionWishDomain
    fun findByUsername(username: String): Optional<MissionWishDomain>
    fun findAll(): List<MissionWishDomain>
    fun update(missionWish: MissionWishDomain): MissionWishDomain

}