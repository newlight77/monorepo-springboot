package io.tricefal.core.pricer

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class PricerReferenceAdapter(private var repository: PricerReferenceJpaRepository) : IPricerReferenceAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun save(pricerRef: PricerReferenceDomain): PricerReferenceDomain {
        repository.all().stream().findFirst().orElseThrow() {
            logger.error("There is no data in the priceer reference table")
            throw NoDataException("There is no data in the pricer reference table")
        }
        val entity = repository.save(toEntity(pricerRef))
        return fromEntity(entity)
    }

    override fun last(): Optional<PricerReferenceDomain> {
        return repository.all().stream().findFirst().map {
            fromEntity(it)
        }
    }

    class NoDataException(private val msg: String) : Throwable(msg) {}
}





