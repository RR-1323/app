package com.example.myapplication.ui.main.Digest

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn

import com.example.myapplication.ui.main.Photo
import com.example.myapplication.ui.main.model.Digest
import com.example.myapplication.ui.main.profile.PhotosPagingUseCaseImpl
import com.example.myapplication.ui.main.profile.Requester
import com.example.myapplication.ui.main.repository.LocalRepositoryImpl
import com.example.myapplication.ui.main.repository.PhotoRemoteRepositoryImpl
import com.example.myapplication.ui.main.state.LoadState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class DigestDetailViewModel(application: Application) : ViewModel() {
    private val photosRepositoryImpl = PhotoRemoteRepositoryImpl(application)
    private val localRepository = LocalRepositoryImpl()
    private val photosRepository =
        com.example.myapplication.ui.main.profile.PhotosPagingSourceRepositoryImpl(
            photosRepositoryImpl,
            localRepository
        )
    private val photosPagingUseCase = PhotosPagingUseCaseImpl(photosRepository)

    private val id = MutableStateFlow("")
    private var job: Job? = null
    val _loadState = MutableStateFlow(LoadState.START)
    val loadState = _loadState.asStateFlow()

    protected val handler = CoroutineExceptionHandler { _, _ ->
        _loadState.value = LoadState.ERROR
    }


    private val _state = MutableStateFlow<DigestState>(DigestState.NotStartedYet)
    val state = _state.asStateFlow()

    fun getDigestInfo(id: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val a = photosRepositoryImpl.getDigestInfo(id = id)
            _loadState.value = LoadState.SUCCESS
            _state.value = DigestState.Success(a)
        }
    }
/*
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getPhoto() = id.asStateFlow()
        .flatMapLatest { photosPagingUseCase.getPhoto(Requester.COLLECTIONS.apply { param = it }) }
        .cachedIn(CoroutineScope(Dispatchers.IO))
*/

    /*  fun like(item: Photo) {
          viewModelScope.launch(Dispatchers.IO + handler) {
              photoLikeUseCase.likePhoto(item)
              _loadState.value = LoadState.SUCCESS
          }
      }*/

    fun setId(newText: String, refresh: () -> Unit) {
        job?.cancel()
        job = viewModelScope.launch {
            id.value = newText
            refresh()
        }
    }
}

sealed class DigestState {
    data class Success(val data: Digest) : DigestState()
    object NotStartedYet : DigestState()
}