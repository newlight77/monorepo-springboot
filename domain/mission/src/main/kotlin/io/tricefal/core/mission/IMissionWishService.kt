package io.tricefal.core.mission

interface IMissionWishService {
    fun create(missionWish: MissionWishDomain) : MissionWishDomain
    fun findByUsername(username: String): MissionWishDomain
    fun findAll(): List<MissionWishDomain>
    fun update(missionWish: MissionWishDomain): MissionWishDomain
    fun updateOnResumeUploaded(username: String, filename: String): MissionWishDomain
}