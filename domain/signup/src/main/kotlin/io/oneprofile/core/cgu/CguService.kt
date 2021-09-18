package io.oneprofile.core.cgu

import org.slf4j.LoggerFactory
import java.util.*

class CguService(private var adapter: ICguAdapter) : ICguService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun findByUsername(username: String): Optional<CguDomain> {
        return adapter.findByUsername(username)
    }

    override fun save(username: String, cguVersion: String): CguDomain {
        val cgu = adapter.findByUsername(username)
        return if (cgu.isPresent) {
            cgu.get().acceptedCguVersion = cguVersion
            adapter.save(cgu.get())
        } else throw NotFoundException("Failed to find a cgu for user $username")
    }

}

class NotFoundException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
    constructor(message: String?) : this(message, null)
}
