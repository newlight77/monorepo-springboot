package io.tricefal.core.freelance

import io.tricefal.core.metafile.IMetafileAdapter
import io.tricefal.core.metafile.IMetafileService
import io.tricefal.core.metafile.MetafileService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FreelanceConfiguration {
//
//    @Bean
//    fun freelanceAdapter(freelanceJpaRepository: FreelanceJpaRepository): IFreelanceAdapter {
//        return FreelanceAdapter(freelanceJpaRepository)
//    }

    @Bean
    fun freelanceService(freelanceAdapter: IFreelanceAdapter): IFreelanceService {
        return FreelanceService(freelanceAdapter)
    }

    @Bean
    fun metafileService(metafileAdapter: IMetafileAdapter): IMetafileService {
        return MetafileService(metafileAdapter)
    }

}