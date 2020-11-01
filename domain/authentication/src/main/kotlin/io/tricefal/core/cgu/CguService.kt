package io.tricefal.core.cgu

import org.slf4j.LoggerFactory
import java.util.*

class CguService(private var adapter: ICguAdapter) : ICguService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun findByUsername(username: String): Optional<CguDomain> {
        return adapter.findByUsername(username)
    }

    override fun save(cgu: CguDomain): CguDomain {
        return adapter.save(cgu)
    }

}