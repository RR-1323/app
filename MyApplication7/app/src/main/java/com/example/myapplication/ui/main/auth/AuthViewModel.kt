package com.example.myapplication.ui.main.auth

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.ui.main.repository.PhotoRemoteRepositoryImpl
import com.example.myapplication.ui.main.state.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    
    private val authService = AuthorizationService(application)
    private val authRepo = AuthRepo(application)
    
    private val _authSuccessEventChannel = Channel<Unit>()
    val authSuccessEventChannel get() = _authSuccessEventChannel.receiveAsFlow()
    
    private val _toastEventChannel = Channel<Int>()
    val toastEventChannel get() = _toastEventChannel.receiveAsFlow()
    
    private val _authCheckFlow = MutableStateFlow<AuthCheckResult<Boolean>>(AuthCheckResult.Loading())
    val authCheckFlow = _authCheckFlow.asStateFlow()
    
//    private val _loadState = MutableStateFlow(LoadState.START)
//    val loadState = _loadState.asStateFlow()
//
//    private val handler = CoroutineExceptionHandler { _, _ ->
//        _loadState.value = LoadState.ERROR
//    }
//    private val _token = MutableSharedFlow<String>()
//    val token = _token.asSharedFlow()
//    private var accessToken = ""
    
    fun prepareAuthPageIntent(openAuthPage: (Intent) -> Unit) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        val authRequest = AppAuth.getAuthorizationRequest()
        
        val authPageIntent = authService.getAuthorizationRequestIntent(
            authRequest,
            customTabsIntent
        )
        Log.d("AAA", "Generated verifier=${authRequest.codeVerifier},challenge=${authRequest.codeVerifierChallenge}")
        openAuthPage(authPageIntent)
    }
    
    fun handleAuthResponseIntent(intent: Intent) {
        val exception = AuthorizationException.fromIntent(intent)
        val tokenExchangeRequest = AuthorizationResponse.fromIntent(intent)
            ?.createTokenExchangeRequest()
        viewModelScope.launch {
            when {
                exception != null -> _toastEventChannel.send(R.string.authentication_failed)
                tokenExchangeRequest != null ->
                    onAuthCodeReceived(tokenExchangeRequest)
            }
        }
    }
    
    private suspend fun onAuthCodeReceived(tokenRequest: TokenRequest) {
        runCatching {
            AppAuth.performTokenRequestSuspend(
                authService = authService,
                tokenRequest = tokenRequest
            )
        }.onSuccess {
            authRepo.saveAccessToken(it)
            Log.d("AAA token", it)
            _authSuccessEventChannel.send(Unit)
        }.onFailure {
            _toastEventChannel.send(R.string.auth_cancel)
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }
    
    fun checkAuthToken() {
        viewModelScope.launch(Dispatchers.IO) {
            _authCheckFlow.value = AuthCheckResult.Loading()
            val result = authRepo.checkAuthToken()
            _authCheckFlow.value = result
        }
    }
    
    fun logOut(openLogoutPage: (Intent) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val endSessionRequest = AppAuth.getLogoutRequest(authRepo.getAccessToken())
            val customTabsIntent = CustomTabsIntent.Builder().build()
            val endSessionIntent = authService.getEndSessionRequestIntent(
                endSessionRequest, customTabsIntent
            )
            authRepo.clearDataStore()
            openLogoutPage(endSessionIntent)
        }
    }
    
 /*  fun getToken(authCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadState.emit(LoadState.START)
            Log.d("onIntent", "getToken ${authCode}")
            kotlin.runCatching {
                retrofitT.getToken(TokenBody(code = authCode))

            }.fold(
                onSuccess = {
                    accessToken = it.access_token
                    Log.d("onIntent", "success token" + it.access_token)
                    _loadState.emit(LoadState.SUCCESS)
                },
                onFailure = {
                    Log.d("onIntent", "error token" + it.message ?: "")
                    _loadState.emit(LoadState.ERROR)
                }
            )
        }
    }
*/
}