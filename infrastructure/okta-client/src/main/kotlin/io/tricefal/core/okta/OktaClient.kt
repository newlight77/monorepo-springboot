package io.tricefal.core.okta

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


interface OktaClient {

    @POST("/api/v1/users?activate=false")
    fun createUser(@Body user: OktaUser): Call<OktaResponse>

    class Builder(val oktaBaseUrl: String) {

        fun build(): OktaClient {
            val retrofit = Retrofit.Builder()
                    .baseUrl(oktaBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(OktaClient::class.java)
        }
    }
}