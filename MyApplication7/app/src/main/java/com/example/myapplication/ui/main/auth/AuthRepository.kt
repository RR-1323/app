package com.example.myapplication.ui.main.auth

import android.util.Log
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

class AuthRepository {

    fun getAuthRequest(): AuthorizationRequest {
        return AppAuth.getAuthorizationRequest()
    }

    suspend fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ) {
        val tokens: TokensModel = AppAuth.performTokenRequestSuspend(authService, tokenRequest)

        TokenStorage.accessToken = tokens.accessToken
        Log.d("AccessToken ", "${TokenStorage.accessToken}")

    }
}


object TokenStorage {
    var accessToken: String? = null
}

data class TokensModel(
    val accessToken: String
)
