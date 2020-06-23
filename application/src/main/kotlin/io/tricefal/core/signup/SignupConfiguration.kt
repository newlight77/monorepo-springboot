package io.tricefal.core.signup

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SignupConfiguration {

    @Bean
    fun signupService(signupAdapter: ISignupAdapter): ISignupService {
        return SignupService(signupAdapter)
    }
}