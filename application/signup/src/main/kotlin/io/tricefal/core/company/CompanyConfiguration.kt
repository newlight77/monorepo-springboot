package io.tricefal.core.company

import io.tricefal.core.metafile.IMetafileDataAdapter
import io.tricefal.core.metafile.IMetafileService
import io.tricefal.core.metafile.MetafileService
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