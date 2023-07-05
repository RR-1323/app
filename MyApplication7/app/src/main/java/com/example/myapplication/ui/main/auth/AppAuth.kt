package com.example.myapplication.ui.main.auth

import androidx.core.net.toUri
import net.openid.appauth.*
import kotlin.coroutines.suspendCoroutine

object AppAuth {
    
    private val serviceConfig = AuthorizationServiceConfiguration(
        AuthConfig.AUTH_URI.toUri(),
        AuthConfig.TOKEN_URI.toUri(),
        null,
        AuthConfig.LOGOUT_URL.toUri()
    )
    
    fun getAuthorizationRequest(): AuthorizationRequest =
        AuthorizationRequest.Builder(
            serviceConfig,
            AuthConfig.CLIENT_ID,
            AuthConfig.RESPONSE_TYPE,
            AuthConfig.REDIRECT_URL.toUri()
        ).setScopes(AuthConfig.SCOPES)
            .build()
    
    suspend fun performTokenRequestSuspend(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ): String {
        return suspendCoroutine { continuation ->
            authService.performTokenRequest(
                tokenRequest,
                getClientAuthentication()
            ) { response, ex ->
                when {
                    response != null -> {
                        val token = response.accessToken.orEmpty()
                        continuation.resumeWith(Result.success(token))
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
        const val AUTH_URI = "https://unsplash.com/oauth/authorize"
        const val TOKEN_URI = "https://unsplash.com/oauth/token"
        const val RESPONSE_TYPE = ResponseTypeValues.CODE
        const val SCOPES =
            "public read_user write_user read_photos write_photos write_likes write_followers read_collections write_collections"
        const val CLIENT_ID = "6V5MrkH8_pUaxqLiS0he3hQqzdywrlzHPNmaVcv1E30"
        const val CLIENT_SECRET = "08_YBIy8kQ-SutfpizrYS5axGulFj9eTTtkGQCwW2rM"
        const val REDIRECT_URL = "com.example.application://auth"
        const val LOGOUT_URL = "https://unsplash.com/logout"
    }
}