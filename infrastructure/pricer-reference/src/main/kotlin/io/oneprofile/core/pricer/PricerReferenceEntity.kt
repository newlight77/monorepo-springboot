package io.oneprofile.core.pricer

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "ref_pricer")
data class PricerReferenceEntity (
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @Column(name = "last_date")
        var lastDate: Instant = Instant.now(),

        @Column(name = "nb_days_year")
        var workDaysPerYear: Int,

        @Column(name = "comm_phase1")
        var commissionFreelancePercentagePhase1: Int,

        @Column(name = "employer_charge")
        var employerChargePercentage: Int,

        @Column(name = "insurance")
        var monthlyInsurance50: Int,

        @Column(name = "resto_daily")
        var restaurantDailyContribution: Int,

        @Column(name = "resto_employer")
        var restaurantDailyEmployerPercentage: Int,

        @Column(name = "navigo_yr_fee")
        var navigoAnnualFee: Int,

        @Column(name = "navigo_yr_employer")
        var navigoAnnualFeeEmployerPercentage: Int,

        @Column(name = "payslip_mtly_fee")
        var paySlipMonthlyFee: Int
)

fun toEntity(domain: PricerReferenceDomain): PricerReferenceEntity {
        return PricerReferenceEntity(
                lastDate = domain.lastDate,
                workDaysPerYear = domain.workDaysPerYear,
                commissionFreelancePercentagePhase1 = domain.commissionFreelancePercentagePhase1,
                employerChargePercentage = domain.employerChargePercentage,
                monthlyInsurance50 = domain.monthlyInsurance50,
                restaurantDailyContribution = domain.restaurantDailyContribution,
                restaurantDailyEmployerPercentage = domain.restaurantDailyEmployerPercentage,
                navigoAnnualFee = domain.navigoAnnualFee,
                navigoAnnualFeeEmployerPercentage = domain.navigoAnnualFeeEmployerPercentage,
                paySlipMonthlyFee = domain.paySlipMonthlyFee
        )
}

fun fromEntity(entity: PricerReferenceEntity): PricerReferenceDomain {
        return PricerReferenceDomain.Builder()
                .lastDate(entity.lastDate)
                .workDaysPerYear(entity.workDaysPerYear)
                .commissionFreelancePercentagePhase1(entity.commissionFreelancePercentagePhase1)
                .employerChargePercentage(entity.employerChargePercentage)
                .monthlyInsurance50(entity.monthlyInsurance50)
                .restaurantDailyContribution(entity.restaurantDailyContribution)
                .restaurantDailyEmployerPercentage(entity.restaurantDailyEmployerPercentage)
                .navigoAnnualFee(entity.navigoAnnualFee)
                .navigoAnnualFeeEmployerPercentage(entity.navigoAnnualFeeEmployerPercentage)
                .paySlipMonthlyFee(entity.paySlipMonthlyFee)
                .build()
}
