package io.tricefal.core.cgu

import io.tricefal.core.exception.NotAcceptedException
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service


@Service
@PropertySource("classpath:application.yml")
class CguWebHandler(val cguService: ICguService,
                        private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun find(username: String): CguModel {
         return toModel(findCgu(username))
    }

    fun acceptCgu(username: String, cguVersion: String): CguModel {
        val cgu = findCgu(username)
        return toModel(cguService.save(cgu))
    }

    private fun findCgu(username: String): CguDomain {
        if (username.isEmpty()) throw NotAcceptedException("username is $username")
        return this.cguService.findByUsername(username)
                .orElseGet { CguDomain.Builder(username).build() }
    }

}
