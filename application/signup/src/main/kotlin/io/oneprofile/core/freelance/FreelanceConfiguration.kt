package io.oneprofile.core.freelance

import io.oneprofile.core.metafile.IMetafileDataAdapter
import io.oneprofile.core.metafile.IMetafileService
import io.oneprofile.core.metafile.MetafileService
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