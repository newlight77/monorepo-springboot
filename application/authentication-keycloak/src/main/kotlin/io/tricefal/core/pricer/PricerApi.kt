package io.tricefal.core.pricer

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed
import kotlin.math.roundToInt


@RestController
@RequestMapping("pricer")
class PricerApi(val cguWebHandler: PricerWebHandler) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun toSalary(@RequestParam fee: Int): Int {
        logger.info("requested a calculation with fee=$fee")
        return cguWebHandler.toSalary(fee).roundToInt()
    }

}