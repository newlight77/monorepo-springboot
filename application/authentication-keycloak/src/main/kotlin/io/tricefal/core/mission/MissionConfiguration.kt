package io.tricefal.core.mission

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MissionConfiguration {

    @Bean
    fun missionWishService(missionWishAdapter: IMissionWishAdapter): IMissionWishService {
        return MissionWishService(missionWishAdapter)
    }

}