package io.oneprofile.signup.company

import io.oneprofile.signup.metafile.IMetafileDataAdapter
import io.oneprofile.signup.metafile.IMetafileService
import io.oneprofile.signup.metafile.MetafileService
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