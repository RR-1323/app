package com.example.myapplication.ui.main.auth

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.ui.main.state.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepos = AuthRepository()
    val authRepo = AuthRepo(application)
    private val authService = AuthorizationService(application)
    private val _loadState = MutableStateFlow(LoadState.START)
    val loadState = _loadState.asStateFlow()
    private val openAuthPageEventChannel = Channel<Intent>(Channel.BUFFERED)
    private val _authSuccessEventChannel = Channel<Unit>()
    val authSuccessEventChannel get() = _authSuccessEventChannel.receiveAsFlow()
    private val _toastEventChannel = Channel<Int>()
    val toastEventChannel get() = _toastEventChannel.receiveAsFlow()
    private val handler = CoroutineExceptionHandler { _, _ ->
        _loadState.value = LoadState.ERROR
    }
    private val _token = MutableSharedFlow<String>()
    val token = _token.asSharedFlow()
    
    //val repository = AuthRepo()
    private var accessToken = ""
    
    fun openLoginPage() {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        val authRequest = authRepos.getAuthRequest()
        val openAuthPageIntent = authService.getAuthorizationRequestIntent(
            authRequest,
            customTabsIntent
        )
        
        openAuthPageEventChannel.trySendBlocking(openAuthPageIntent)
    }
    
    fun prepareAuthPageIntent(openAuthPage: (Intent) -> Unit) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        val authRequest = AppAuth.getAuthorizationRequest()
        val authPageIntent = authService.getAuthorizationRequestIntent(
            authRequest,
            customTabsIntent
        )
        Log.d(
            "prepareAuthPageIntent",
            "Generated verifier=${authRequest.codeVerifier},challenge=${authRequest.codeVerifierChallenge}"
        )
        openAuthPage(authPageIntent)
    }
    
    fun handleAuthResponseIntent(intent: Intent) {
        val exception = AuthorizationException.fromIntent(intent)
        val tokenExchangeRequest = AppAuth.prepareTokenRequest(intent)
        
        viewModelScope.launch {
            if (exception != null) {
                _toastEventChannel.send(R.string.authentication_failed)
            } else {
                onAuthCodeReceived(tokenExchangeRequest)
            }
        }
    }
    
    private suspend fun onAuthCodeReceived(tokenRequest: TokenRequest) {
        runCatching {
            authRepos.performTokenRequest(
                authService = authService,
                tokenRequest = tokenRequest
            )
        }.onSuccess {
            _token.emit(accessToken)
            Log.d(
                "onAuthCodeReceived token-------------------------------------------------------------------------------------------------------",
                it.toString()
            )
            Log.d(
                "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------AuthFragment",
                it.toString()
            )
            _authSuccessEventChannel.send(Unit)
            
        }.onFailure {
            _toastEventChannel.send(R.string.auth_cancel)
            Log.d(
                "Отмена---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------AuthFragment",
                ""
            )
        }
    }
}