package io.tricefal.core.okta

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface OktaClient {

    @Headers("Accept: application/json",
                    "Content-Type: application/json")
    @POST("/api/v1/users?activate=true")
    fun createUser(@Body user: OktaUser): Call<OktaResponse>

    class Builder(private val oktaBaseUrl: String) {

        private var client: OkHttpClient? = null

        fun apiToken(apiToken: String) =  apply {
            this.client = OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder().addHeader("Authorization", "SSWS $apiToken").build()
                        chain.proceed(request)
                    }
                    .build()
        }

        fun build(): OktaClient {
            val retrofit = Retrofit.Builder()
                    .baseUrl(oktaBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client!!)
                    .build()
            return retrofit.create(OktaClient::class.java)
        }
    }
//
//    fun create(baseUrl: String, apiToken: String): OktaClient {
//        val client = OkHttpClient.Builder()
//                .addInterceptor { chain ->
//                    val request = chain.request().newBuilder().addHeader("Authorization", "SSWS $apiToken").build()
//                    chain.proceed(request)
//                }
//                .build()
//
//        val retrofit = Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build()
//
//        return retrofit.create(OktaClient::class.java)
//    }
}