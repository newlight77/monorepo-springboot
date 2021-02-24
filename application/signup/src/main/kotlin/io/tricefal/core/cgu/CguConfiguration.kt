package io.tricefal.core.cgu

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CguConfiguration {

    @Bean
    fun cguService(cguAdapter: ICguAdapter): ICguService {
        return CguService(cguAdapter)
    }

}