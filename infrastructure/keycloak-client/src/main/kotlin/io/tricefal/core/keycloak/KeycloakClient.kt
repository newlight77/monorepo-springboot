package io.tricefal.core.keycloak

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


interface KeycloakClient {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/auth/realms/{realm}/protocol/openid-connect/token")
    fun login(@Path("realm") realm: String,
              @Field("grant_type") grantType: String,
              @Field("client_id") clientId: String,
              @Field("client_secret") clientSecret: String,
              @Field("username") username: String,
              @Field("password") password: String): Call<KeycloakTokenResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/auth/realms/{realm}/protocol/openid-connect/token")
    fun clientToken(@Path("realm") realm: String,
              @Field("grant_type") grantType: String,
              @Field("client_id") clientId: String,
              @Field("client_secret") clientSecret: String): Call<KeycloakTokenResponse>

    @Headers("Accept: application/json",
                    "Content-Type: application/json")
    @POST("/auth/admin/realms/{realm}/users")
    fun createUser(@Path("realm") realm: String,
                   @Header("Authorization") bearerToken: String,
                   @Body user: KeycloakNewUser): Call<KeycloakRegisterResponse>


    class Builder(private val keycloakBaseUrl: String) {

        private var client: OkHttpClient = OkHttpClient.Builder().build()

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
//                    .addConverterFactory(MoshiConverterFactory.create())
                    .client(client)
                    .build()
            return retrofit.create(KeycloakClient::class.java)
        }
    }

}