package io.oneprofile.signup.profile

import io.oneprofile.signup.metafile.IMetafileDataAdapter
import io.oneprofile.signup.metafile.IMetafileService
import io.oneprofile.signup.metafile.MetafileService
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