package io.oneprofile.core.user

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserConfiguration {

    @Bean("userService")
    fun userService(userDataAdapter: UserDataAdapter): IUserService {
        return UserService(userDataAdapter)
    }
}