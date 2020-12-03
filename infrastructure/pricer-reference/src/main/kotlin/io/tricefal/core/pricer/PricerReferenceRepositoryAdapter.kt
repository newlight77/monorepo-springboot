package io.tricefal.core.pricer

import java.time.Instant
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class PricerReferenceRepositoryAdapter(private var repository: PricerReferenceJpaRepository) : IPricerReferenceAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun save(pricerRef: PricerReferenceDomain): PricerReferenceDomain {
        repository.all().stream().findFirst().orElse {
//            logger.error("There is no data in the priceer reference table, insert default")
            defaultRef()
        }
        val entity = repository.save(toEntity(pricerRef))
        return fromEntity(entity)
    }

    override fun last(): Optional<PricerReferenceDomain> {
        return repository.all().stream().findFirst().map {
            fromEntity(it)
        }
    }

    private fun defaultRef(): PricerReferenceEntity {
        return PricerReferenceEntity(
                lastDate = Instant.now(),
                workDaysPerYear = 217,
                commissionFreelancePercentagePhase1 = 10,
                employerChargePercentage = 45,
                monthlyInsurance50 = 50,
                restaurantDailyContribution = 10,
                restaurantDailyEmployerPercentage = 50,
                navigoAnnualFee = 827,
                navigoAnnualFeeEmployerPercentage = 50
        )
    }

    class NoDataException(private val msg: String) : Throwable(msg) {}
}





