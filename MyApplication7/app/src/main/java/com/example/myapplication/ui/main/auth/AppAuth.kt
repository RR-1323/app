package com.example.myapplication.ui.main.auth

import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenRequest
import kotlin.coroutines.suspendCoroutine

object AppAuth {
    
    private val serviceConfig = AuthorizationServiceConfiguration(
        AuthConfig.AUTH_URI.toUri(),
        AuthConfig.TOKEN_URI.toUri(),
        null,
        AuthConfig.LOGOUT_URL.toUri()
    )
    
    fun prepareTokenRequest(intent: Intent): TokenRequest {
        val code = intent.dataString?.substringAfter("code=")?.substringBefore('#')
        Log.d("AAA code", code.toString())
        return TokenRequest.Builder(serviceConfig, AuthConfig.CLIENT_ID)
            .setGrantType(AuthConfig.GRANT_TYPE_CODE)
            .setAuthorizationCode(code)
            .setRedirectUri(AuthConfig.REDIRECT_URL.toUri())
            .build()
    }
    
    fun getAuthorizationRequest(): AuthorizationRequest =
        AuthorizationRequest.Builder(
            serviceConfig,
            AuthConfig.CLIENT_ID,
            AuthConfig.RESPONSE_TYPE,
            AuthConfig.REDIRECT_URL.toUri(),
        ).setScopes(AuthConfig.SCOPES)
            .setAdditionalParameters(mapOf("duration" to "permanent"))
            .setState(AuthConfig.STATE)
            .build()
    
    suspend fun performTokenRequestSuspend(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ): TokensModel {
        return suspendCoroutine { continuation ->
            authService.performTokenRequest(
                tokenRequest,
                ClientSecretBasic(AuthConfig.CLIENT_SECRET)
            ) { response, ex ->
                when {
                    response != null -> {
                        val tokens = TokensModel(
                            accessToken = response.accessToken.orEmpty()
                        )
                        
                        val token = response.accessToken.orEmpty()
                        Log.d(
                            "performTokenRequestSuspend------------------------------------------------",
                            token
                        )
                        continuation.resumeWith(Result.success(tokens))
                        //   continuation.resumeWith(Result.success(token))
                    }
                    
                    ex != null -> {
                        continuation.resumeWith(Result.failure(ex))
                    }
                    
                    else -> error("unreachable")
                }
            }
        }
    }
    
    fun getLogoutRequest(token: String?): EndSessionRequest {
        return EndSessionRequest.Builder(serviceConfig)
            .setPostLogoutRedirectUri(AuthConfig.REDIRECT_URL.toUri())
            .setIdTokenHint(token)
            .build()
    }
    
    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(AuthConfig.CLIENT_SECRET)
    }
    
    object AuthConfig {
        const val AUTH_URI = "https://old.reddit.com/api/v1/authorize"
        const val TOKEN_URI = "https://www.reddit.com/api/v1/access_token"
        const val RESPONSE_TYPE = ResponseTypeValues.CODE
        const val SCOPES =
            "identity edit flair history modconfig modflair modlog modposts modwiki mysubreddits privatemessages read report save submit subscribe vote wikiedit wikiread"
        
        const val CLIENT_ID = "0GdjXHpIJNapuBsmy1YKLw"
        // "1BjLeIvC8XdjXE7v86GJ8Q"
        
        const val CLIENT_SECRET = ""
        
        //    "kE9fdTwoiIh0c50XqjIFWQAXbbw9pw"
        const val REDIRECT_URL = "com.example.application://auth"
        const val LOGOUT_URL = "https://www.reddit.com/api/v1/revoke_token"
        const val STATE = "6HII4VwlYgiUH0n68_hQEw"
        
        // "com.example.application:state"
        const val GRANT_TYPE_CODE = "authorization_code"
    }
}

/*fun authorizationUrl(): Uri {
    code = UUID.randomUUID().toString()
    return  Uri.parse(authorizeUri)
        .buildUpon()
        .appendQueryParameter("client_id", clientId)
        .appendQueryParameter("response_type", "code")
        .appendQueryParameter("state")
}*/