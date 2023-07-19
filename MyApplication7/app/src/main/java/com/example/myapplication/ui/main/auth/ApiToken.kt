package com.example.myapplication.ui.main.auth


import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url
/*
interface ApiToken {
    @POST("https://oauth.reddit.com/api/v1/access_token")
    suspend fun getToken(


        @Query("grant_type") grantType: String,
        @Query("code") code: String? = null,
        @Query("redirect_uri") redirectUrl: String? = null,
        @Query("username") username: String? = null,
        @Query("password") password: String? = null,
        @Query("duration") duration: String? = null,
        @Query("device_id") deviceId: String? = null

    ): AuthInfo
}
*/
class AuthInfo(
    val access_token: String
)


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
const val TOKEN_SHARED_NAMEs = "pref_tokens"

const val TOKEN_SHARED_NAME = "pref_token"
const val TOKEN_SHARED_KEY = "token"
const val TOKEN_ENABLED_KEY = "token_enabled"
const val ONBOARDING_IS_SHOWN = "onboarding_is_shown"





