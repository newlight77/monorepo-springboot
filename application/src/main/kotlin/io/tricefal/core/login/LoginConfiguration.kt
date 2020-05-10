package io.tricefal.core.login

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoginConfiguration {

    @Bean("loginService")
    fun loginService(loginRepository: ILoginRepository): ILoginService {
        return LoginService(loginRepository)
    }
}