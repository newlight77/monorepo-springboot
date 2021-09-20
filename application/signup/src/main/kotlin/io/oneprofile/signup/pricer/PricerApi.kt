package io.oneprofile.signup.pricer

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import kotlin.math.roundToInt


@RestController
@RequestMapping("pricer")
class PricerApi(val cguWebHandler: PricerWebHandler) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun toSalary(@RequestParam myDailyFee: Int): Int {
        logger.info("requested a calculation with fee=$myDailyFee")
        return cguWebHandler.toSalary(myDailyFee).roundToInt()
    }

}