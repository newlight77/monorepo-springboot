package io.tricefal.core.pricer

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.annotation.security.RolesAllowed
import kotlin.math.roundToInt


@RestController
@RequestMapping("pricer")
class PricerApi(val cguWebHandler: PricerWebHandler) {

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    fun toSalary(@RequestParam myDailyFee: Int): Int {
        return cguWebHandler.toSalary(myDailyFee).roundToInt()
    }

}