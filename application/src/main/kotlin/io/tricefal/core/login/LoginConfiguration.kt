package io.tricefal.core.login

import io.tricefal.core.login.domain.LoginDomain
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoginConfiguration {

    @Bean("loginService")
    fun loginService(loginRepository: ILoginRepository<LoginDomain, Long>): ILoginService<LoginDomain, Long> {
        return LoginService(loginRepository)
    }
}