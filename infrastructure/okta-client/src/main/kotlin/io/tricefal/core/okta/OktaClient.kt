package io.tricefal.core.okta

import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST


interface OktaClient {

    @POST("/api/v1/users?activate=false")
    fun createUser(@Body user: OktaUser): OktaResponse

    class Builder(val oktaBaseUrl: String) {

        fun build(): OktaClient {
            val retrofit = Retrofit.Builder()
                    .baseUrl(oktaBaseUrl)
                    .build()
            return retrofit.create(OktaClient::class.java)
        }
    }
}