package com.example.myapplication.ui.main.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.main.repository.PhotoRemoteRepositoryImpl
import com.example.myapplication.ui.main.state.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    protected val _loadState = MutableStateFlow(LoadState.START)
    val loadState = _loadState.asStateFlow()

    protected val handler = CoroutineExceptionHandler { _, _ ->
        _loadState.value = LoadState.ERROR
    }
    private val _token = MutableSharedFlow<String>()
    val token = _token.asSharedFlow()
    //val repository = AuthRepo()
    private var accessToken = ""
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