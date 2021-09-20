package io.oneprofile.signup.freelance

import io.oneprofile.signup.metafile.IMetafileDataAdapter
import io.oneprofile.signup.metafile.IMetafileService
import io.oneprofile.signup.metafile.MetafileService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FreelanceConfiguration {

    @Bean
    fun freelanceService(freelanceDataAdapter: FreelanceDataAdapter): IFreelanceService {
        return FreelanceService(freelanceDataAdapter)
    }

    @Bean
    fun metafileService(metafileDataAdapter: IMetafileDataAdapter): IMetafileService {
        return MetafileService(metafileDataAdapter)
    }

}