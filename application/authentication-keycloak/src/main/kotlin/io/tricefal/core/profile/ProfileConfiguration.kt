package io.tricefal.core.profile

import io.tricefal.core.metafile.IMetafileDataAdapter
import io.tricefal.core.metafile.IMetafileService
import io.tricefal.core.metafile.MetafileService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProfileConfiguration {

    @Bean
    fun profileService(profileDataAdapter: ProfileDataAdapter): IProfileService {
        return ProfileService(profileDataAdapter)
    }

    @Bean
    fun metafileService(metafileDataAdapter: IMetafileDataAdapter): IMetafileService {
        return MetafileService(metafileDataAdapter)
    }

}