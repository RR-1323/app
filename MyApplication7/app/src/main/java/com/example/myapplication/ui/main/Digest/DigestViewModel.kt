package com.example.myapplication.ui.main.Digest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.myapplication.ui.main.state.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DigestViewModel(application: Application) :AndroidViewModel(application) {

val digestPagingUseImpl = DigestPagingImpl(application)
 val _loadState = MutableStateFlow(LoadState.START)
    val loadState = _loadState.asStateFlow()

    protected val handler = CoroutineExceptionHandler { _, _ ->
        _loadState.value = LoadState.ERROR
    }

    fun getDigest() = digestPagingUseImpl.getDigest().flow
}