package io.tricefal.core.signup

import io.tricefal.core.metafile.IMetafileDataAdapter
import io.tricefal.core.metafile.IMetafileService
import io.tricefal.core.metafile.MetafileService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SignupConfiguration {

    @Bean
    fun signupService(signupDataAdapter: SignupDataAdapter): ISignupService {
        return SignupService(signupDataAdapter)
    }

    @Bean
    fun metafileService(metafileDataAdapter: IMetafileDataAdapter): IMetafileService {
        return MetafileService(metafileDataAdapter)
    }

}