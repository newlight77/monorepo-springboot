package io.tricefal.core.mission

import io.tricefal.core.metafile.MetafileDomain
import java.util.*

interface IMissionWishService {
    fun create(missionWish: MissionWishDomain) : MissionWishDomain
    fun findByUsername(username: String): Optional<MissionWishDomain>
    fun findAll(): List<MissionWishDomain>
    fun update(missionWish: MissionWishDomain): MissionWishDomain
    fun updateOnResumeUploaded(username: String, metafile: MetafileDomain): MissionWishDomain
}