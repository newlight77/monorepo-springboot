package io.tricefal.core.okta

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.tricefal.core.signup.SignupDomain
import okhttp3.ResponseBody
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service
@PropertySource("classpath:okta.yml")
class OktaService(private val env: Environment) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val baseUrl = env.getProperty("okta.baseUrl")
    private val apiToken = env.getProperty("okta.apiToken")
    private val oktaClient: OktaClient = OktaClient.Builder(baseUrl!!).apiToken(apiToken!!).build()

    fun register(signup: SignupDomain): Boolean {
        val user = toOktaUser(signup)
        val response = oktaClient.createUser(user)
        val result = response.execute()

        print(Gson().toJson(user))

        logger.info("okta code : ${result.code()}")
        logger.info("okta raw : ${result.raw()}")

        if (result.code() != 200 and 201) {
            logger.error("registration failed with : ${result.raw()}")
            throw OktaUnsuccessfulRegistration("the response code is: " + result.raw())
        }

        return "ACTIVE" == result.body()?.created
    }

}

class OktaUnsuccessfulRegistration(val msg: String) : Throwable(msg) {
}
