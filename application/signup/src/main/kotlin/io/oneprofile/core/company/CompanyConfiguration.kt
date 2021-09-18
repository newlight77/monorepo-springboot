package io.oneprofile.core.company

import io.oneprofile.core.metafile.IMetafileDataAdapter
import io.oneprofile.core.metafile.IMetafileService
import io.oneprofile.core.metafile.MetafileService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CompanyConfiguration {

    @Bean
    fun companyService(companyDataAdapter: CompanyDataAdapter): ICompanyService {
        return CompanyService(companyDataAdapter)
    }

    @Bean
    fun metafileService(metafileDataAdapter: IMetafileDataAdapter): IMetafileService {
        return MetafileService(metafileDataAdapter)
    }

}