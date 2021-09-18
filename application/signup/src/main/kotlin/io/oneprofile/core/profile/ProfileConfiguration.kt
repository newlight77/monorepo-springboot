package io.oneprofile.core.profile

import io.oneprofile.core.metafile.IMetafileDataAdapter
import io.oneprofile.core.metafile.IMetafileService
import io.oneprofile.core.metafile.MetafileService
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