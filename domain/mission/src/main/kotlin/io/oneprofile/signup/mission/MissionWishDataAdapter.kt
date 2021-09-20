package io.oneprofile.signup.mission

import java.util.*

interface MissionWishDataAdapter {
    fun create(missionWish: MissionWishDomain): MissionWishDomain
    fun findByUsername(username: String): Optional<MissionWishDomain>
    fun findAll(): List<MissionWishDomain>
    fun update(missionWish: MissionWishDomain): MissionWishDomain
    fun updateOnResumeUploaded(username: String, filename: String)
}