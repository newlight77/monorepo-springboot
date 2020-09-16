package io.tricefal.core.profile

import io.tricefal.core.metafile.IMetafileAdapter
import io.tricefal.core.metafile.IMetafileService
import io.tricefal.core.metafile.MetafileService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProfileConfiguration {

    @Bean
    fun profileService(profileAdapter: IProfileAdapter): IProfileService {
        return ProfileService(profileAdapter)
    }

    @Bean
    fun metafileService(metafileAdapter: IMetafileAdapter): IMetafileService {
        return MetafileService(metafileAdapter)
    }

}