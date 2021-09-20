package io.oneprofile.signup.pricer

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Service


@Service
@PropertySource("classpath:application.yml")
class PricerWebHandler(val pricerService: IPricerService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun toSalary(myDailyFee: Int): Double {
         return pricerService.toSalary(myDailyFee)
    }

}
