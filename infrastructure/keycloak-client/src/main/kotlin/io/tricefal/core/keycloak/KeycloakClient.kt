package io.tricefal.core.keycloak

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface KeycloakClient {

    @Headers("Accept: application/json",
                    "Content-Type: application/json")
    @POST("/auth/realms/{realm}/protocol/openid-connect/token")
    fun login(@Path("realm") realm: String,
              @Body user: KeycloakPasswordGrantType): Call<KeycloakTokenResponse>


    @Headers("Accept: application/json",
                    "Content-Type: application/json")
    @POST("/auth/admin/realms/{realm}/users")
    fun createUser(@Path("realm") realm: String,
                   @Body user: KeycloakNewUser): Call<KeycloakRegisterResponse>


    class Builder(private val keycloakBaseUrl: String, private val realm: String) {

        private var client: OkHttpClient = OkHttpClient.Builder().build()
        private var grantType: String? = null
        private var clientId: String? = null
        private var clientSecret: String? = null

        fun grantType(grantType: String) = apply { this.grantType = grantType }
        fun clientId(clientId: String) = apply { this.clientId = clientId }
        fun clientSecret(clientSecret: String) = apply { this.clientSecret = clientSecret }

        fun token(token: String) =  apply {
            this.client = OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request()
                                .newBuilder().addHeader("Authorization", "Bearer $token").build()
                        chain.proceed(request)
                    }
                    .build()
        }

        fun build(): KeycloakClient {
            val retrofit = Retrofit.Builder()
                    .baseUrl(keycloakBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            return retrofit.create(KeycloakClient::class.java)
        }
    }

}