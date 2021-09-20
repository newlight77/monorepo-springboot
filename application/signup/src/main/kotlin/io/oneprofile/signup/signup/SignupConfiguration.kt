package io.oneprofile.signup.signup

import io.oneprofile.signup.metafile.IMetafileDataAdapter
import io.oneprofile.signup.metafile.IMetafileService
import io.oneprofile.signup.metafile.MetafileService
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