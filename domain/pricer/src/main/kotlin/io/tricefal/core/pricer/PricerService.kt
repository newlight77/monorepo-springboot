package io.tricefal.core.pricer

import org.slf4j.LoggerFactory

class PricerService(private var adapter: IPricerReferenceAdapter) : IPricerService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun toSalary(myDailyFee: Int): Double {
        val pr = adapter.last().orElseThrow() {
            throw PricerToSalaryException("the pricer can not convert the fee=$myDailyFee to salary right now. Please try again later...")
        }

        return pr.income(myDailyFee) - pr.insuranceFee() - pr.restoFee() - pr.navigoFee()
    }

}

class PricerToSalaryException(val s: String) : Throwable()
