package io.tricefal.core.pricer

import java.time.Instant

data class PricerReferenceDomain
    constructor(
            var lastDate: Instant,
            var workDaysPerYear: Int,
            var commissionFreelancePercentagePhase1: Int,
            var employerChargePercentage: Int,
            var monthlyInsurance50: Int,
            var restaurantDailyContribution: Int,
            var restaurantDailyEmployerPercentage: Int,
            var navigoAnnualFee: Int,
            var navigoAnnualFeeEmployerPercentage: Int,
            var paySlipMonthlyFee: Int
        ) {

    fun income(myFee: Int): Double
            = (myFee * workDaysPerYear) * (1.0 - (commissionFreelancePercentagePhase1.toDouble()/100)) / (1.0 + (employerChargePercentage.toDouble()/100))

    fun insuranceFee(): Int
            = 12 * monthlyInsurance50

    fun restoFee(): Double
            = workDaysPerYear * restaurantDailyContribution * (restaurantDailyEmployerPercentage.toDouble()/100)

    fun navigoFee(): Double
            = navigoAnnualFee * (navigoAnnualFeeEmployerPercentage.toDouble()/100)

    fun paySlipFee(): Int
            = 12 * paySlipMonthlyFee

    data class Builder(
            var lastDate: Instant? = null,
            var workDaysPerYear: Int? = null,
            var commissionFreelancePercentagePhase1: Int? = null,
            var employerChargePercentage: Int? = null,
            var monthlyInsurance50: Int? = null,
            var restaurantDailyContribution: Int? = null,
            var restaurantDailyEmployerPercentage: Int? = null,
            var navigoAnnualFee: Int? = null,
            var navigoAnnualFeeEmployerPercentage: Int? = null,
            var paySlipMonthlyFee: Int? = null,
    ) {
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }
        fun workDaysPerYear(workDaysPerYear: Int?) = apply { this.workDaysPerYear = workDaysPerYear }
        fun commissionFreelancePercentagePhase1(commissionFreelancePercentagePhase1: Int?) = apply { this.commissionFreelancePercentagePhase1 = commissionFreelancePercentagePhase1 }
        fun employerChargePercentage(employerChargePercentage: Int?) = apply { this.employerChargePercentage = employerChargePercentage }
        fun monthlyInsurance50(monthlyInsurance50: Int?) = apply { this.monthlyInsurance50 = monthlyInsurance50 }
        fun restaurantDailyContribution(restaurantDailyContribution: Int?) = apply { this.restaurantDailyContribution = restaurantDailyContribution }
        fun restaurantDailyEmployerPercentage(restaurantDailyEmployerPercentage: Int?) = apply { this.restaurantDailyEmployerPercentage = restaurantDailyEmployerPercentage }
        fun navigoAnnualFee(navigoAnnualFee: Int?) = apply { this.navigoAnnualFee = navigoAnnualFee }
        fun navigoAnnualFeeEmployerPercentage(navigoAnnualFeeEmployerPercentage: Int?) = apply { this.navigoAnnualFeeEmployerPercentage = navigoAnnualFeeEmployerPercentage }
        fun paySlipMonthlyFee(paySlipMonthlyFee: Int?) = apply { this.paySlipMonthlyFee = paySlipMonthlyFee }

        fun build() = PricerReferenceDomain(
            lastDate = lastDate!!,
            workDaysPerYear = workDaysPerYear!!,
            commissionFreelancePercentagePhase1 = commissionFreelancePercentagePhase1!!,
            employerChargePercentage = employerChargePercentage!!,
            monthlyInsurance50 = monthlyInsurance50!!,
            restaurantDailyContribution = restaurantDailyContribution!!,
            restaurantDailyEmployerPercentage = restaurantDailyEmployerPercentage!!,
            navigoAnnualFee = navigoAnnualFee!!,
            navigoAnnualFeeEmployerPercentage = navigoAnnualFeeEmployerPercentage!!,
            paySlipMonthlyFee = paySlipMonthlyFee!!
        )
    }
}

