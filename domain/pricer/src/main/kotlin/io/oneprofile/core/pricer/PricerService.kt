package io.oneprofile.core.pricer

import org.slf4j.LoggerFactory
import java.time.Instant

class PricerService(private var adapter: IPricerReferenceAdapter) : IPricerService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun toSalary(myDailyFee: Int): Double {
        val pr =adapter.last().orElse(defaultRef())

        return pr.income(myDailyFee) - pr.insuranceFee() - pr.restoFee() - pr.navigoFee() - pr.paySlipFee()
    }

    private fun defaultRef(): PricerReferenceDomain {
        return PricerReferenceDomain(
            lastDate = Instant.now(),
            workDaysPerYear = 217,
            commissionFreelancePercentagePhase1 = 10,
            employerChargePercentage = 45,
            monthlyInsurance50 = 50,
            restaurantDailyContribution = 10,
            restaurantDailyEmployerPercentage = 50,
            navigoAnnualFee = 827,
            navigoAnnualFeeEmployerPercentage = 50,
            paySlipMonthlyFee = 20
        )
    }

}

class PricerToSalaryException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
