package io.tricefal.core.okta

import io.tricefal.core.signup.SignupDomain
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OktaService(private val oktaProperties: OktaProperties) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val oktaClient: OktaClient = OktaClient.Builder(oktaProperties.baseUrl).build()

    fun register(signup: SignupDomain) {
        val response = oktaClient.createUser(toOktaUser(signup))
        logger.info(response.toString())
    }

}