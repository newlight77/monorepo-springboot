package io.tricefal.core.freelance

import io.tricefal.core.metafile.IMetafileDataAdapter
import io.tricefal.core.metafile.IMetafileService
import io.tricefal.core.metafile.MetafileService
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