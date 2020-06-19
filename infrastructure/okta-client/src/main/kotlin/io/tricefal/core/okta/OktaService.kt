package io.tricefal.core.okta

import io.tricefal.core.signup.SignupDomain
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service
@PropertySource("classpath:okta.yml")
class OktaService(private val env: Environment) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val baseUrl = env.getProperty("okta.baseUrl")
    private val oktaClient: OktaClient = OktaClient.Builder(baseUrl!!).build()

    fun register(signup: SignupDomain) {
        val response = oktaClient.createUser(toOktaUser(signup))
        logger.info(response.toString())
    }

}