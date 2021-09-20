package io.oneprofile.signup.command

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommandConfiguration {

    @Bean
    fun commandService(companyDataAdapter: CommandDataAdapter): ICommandService {
        return CommandService(companyDataAdapter)
    }

}