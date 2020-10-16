package io.tricefal.core.freelance

import io.tricefal.core.metafile.IMetafileAdapter
import io.tricefal.core.metafile.IMetafileService
import io.tricefal.core.metafile.MetafileService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FreelanceConfiguration {

    @Bean
    fun freelanceService(signupAdapter: IFreelanceAdapter): IFreelanceService {
        return FreelanceService(signupAdapter)
    }

    @Bean
    fun metafileService(metafileAdapter: IMetafileAdapter): IMetafileService {
        return MetafileService(metafileAdapter)
    }

}