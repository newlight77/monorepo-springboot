package io.tricefal.core.pricer

import java.time.Instant
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class PricerReferenceRepositoryAdapter(private var repository: PricerReferenceJpaRepository) : IPricerReferenceAdapter {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun save(pricerRef: PricerReferenceDomain): PricerReferenceDomain {
        val entity = repository.save(toEntity(pricerRef))
        entity.lastDate = pricerRef.lastDate
        return fromEntity(entity)
    }

    override fun last(): Optional<PricerReferenceDomain> {
        return repository.all().stream().findFirst()
            .map { fromEntity(it) }
    }

    class NoDataException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
}





