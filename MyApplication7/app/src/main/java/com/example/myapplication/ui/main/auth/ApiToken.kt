package com.example.myapplication.ui.main.auth


import com.example.myapplication.ui.main.ApiPhotos
import com.example.myapplication.ui.main.auth.interceptor.AuthTokenInterceptor
import com.example.myapplication.ui.main.okhttpClient

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/*interface ApiToken {
   @POST("https://api.unsplash.com/oauth/token")
  // @POST("oauth/token")
    suspend fun getToken(@Body tokenData : TokenBody): AuthInfo
}*/
interface ApiToken {
    @POST("https://api.unsplash.com/oauth/token")
    suspend fun getToken(
        @Query("code") code: String,
        @Query("client_id") clientId: String = ACCESS_KEY,
        @Query("client_secret") clientSecret: String = SECRET_KEY,
        @Query("redirect_uri") redirectUri: String = REDIRECT_URI,
        @Query("grant_type") grantType: String = "authorization_code"
    ): AuthInfo
}
class AuthInfo(
    val access_token: String
)
/*
class TokenBody(
    val client_id: String = ACCESS_KEY,
    val client_secret : String = SECRET_KEY,
val redirect_uri : String = REDIRECT_URI,
    val code: String = authcode,
val grantType: String = "authorization_code"
)
*/
/*val retrofitTok = Retrofit.Builder()
    .baseUrl("https://api.unsplash.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC))
        .addInterceptor(AuthTokenInterceptor())
        .followRedirects(true)
        .build())
    .build()
    .create(ApiToken::class.java)*/



val retrofitT = Retrofit.Builder()
    .baseUrl("https://api.unsplash.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(okhttpClient)
    .build()
    .create(ApiToken::class.java)

const val ACCESS_KEY = "6V5MrkH8_pUaxqLiS0he3hQqzdywrlzHPNmaVcv1E30"
const val SECRET_KEY = "08_YBIy8kQ-SutfpizrYS5axGulFj9eTTtkGQCwW2rM"
//const val REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob"
const val REDIRECT_URI = "com.example.application://auth"
const val SCOPE =
    "public+read_user+" +
            "write_user+read_photos+" +
            "write_photos+" +
            "write_likes+" +
            "write_followers+" +
            "read_collections+" +
            "write_collections"

const val CALL =
    "https://unsplash.com/oauth/authorize" +
            "?client_id=" + ACCESS_KEY +
            "&redirect_uri=" + REDIRECT_URI +
            "&response_type=code" +
            "&scopes=" + SCOPE


const val TOKEN_SHARED_NAME = "pref_token"
const val TOKEN_SHARED_KEY = "token"
const val TOKEN_ENABLED_KEY = "token_enabled"
const val ONBOARDING_IS_SHOWN = "onboarding_is_shown"





