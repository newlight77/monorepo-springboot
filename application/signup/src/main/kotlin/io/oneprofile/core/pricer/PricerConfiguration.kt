package io.oneprofile.core.pricer

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PricerConfiguration {

    @Bean
    fun pricerService(pricerAdapter: IPricerReferenceAdapter): IPricerService {
        return PricerService(pricerAdapter)
    }

}