package com.example.myapplication.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn

import com.example.myapplication.ui.main.repository.PhotoRemoteRepositoryImpl
import com.example.myapplication.ui.main.repository.PhotosPagingSourceRepositoryImpl
import com.example.myapplication.ui.main.state.LoadState
import kotlinx.coroutines.*

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class MainViewModel(application: Application) : ViewModel() {
    private val query = MutableStateFlow("")
    private var job: Job? = null
    protected val _loadState = MutableStateFlow(LoadState.START)
    val loadState = _loadState.asStateFlow()
    protected val handler = CoroutineExceptionHandler { _, _ ->
        _loadState.value = LoadState.ERROR
    }
    val _throwable = MutableLiveData<Throwable?>(null)
    val throwable: LiveData<Throwable?> = _throwable
    val repo = PhotoRemoteRepositoryImpl(application)
    val photosPagingSourceRepositoryImpl = PhotosPagingSourceRepositoryImpl(application)

    var _isLoading = MutableStateFlow(false)

    //  val filterEnabled = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

  /*  fun getPhoto() = query.asStateFlow()
        .flatMapLatest { photosPagingSourceRepositoryImpl.getFlowPhoto() }
        .cachedIn(CoroutineScope(Dispatchers.IO))*/

    fun like(item: Photo) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            if (item.likedByUser) {
                repo.likePhoto(item.id)
                _loadState.value = LoadState.SUCCESS
            }
        }
    }


    fun setQuery(newText: String, refresh: () -> Unit) {
        job?.cancel()
        job = viewModelScope.launch {
            delay(500)
            query.value = newText
            refresh()
        }
    }


}

