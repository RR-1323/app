package com.example.myapplication.ui.main.profile

import Profile
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.example.myapplication.ui.main.Photo
import com.example.myapplication.ui.main.repository.*
import com.example.myapplication.ui.main.responses.me.MeResponse
import com.example.myapplication.ui.main.state.LoadState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ProfileViewModel(application: Application) : ViewModel() {
    private val photosRepositoryImpl = PhotoRemoteRepositoryImpl(application)
    private val localRepository = LocalRepositoryImpl()
    private val photosRepository =
        com.example.myapplication.ui.main.profile.PhotosPagingSourceRepositoryImpl(
            photosRepositoryImpl,
            localRepository
        )
    private val photosPagingUseCase = PhotosPagingUseCaseImpl(photosRepository)
    private val query = MutableStateFlow("")



    private val userName = MutableStateFlow("")
    private var job: Job? = null

    private val _profile = MutableSharedFlow<Profile>()
    val profile = _profile.asSharedFlow()

    private val _state = MutableStateFlow<ProfileState>(ProfileState.NotStartedYet)
    val state = _state.asStateFlow()

    val _loadState = MutableStateFlow(LoadState.START)
    val loadState = _loadState.asStateFlow()

    val handler = CoroutineExceptionHandler { _, _ ->
        _loadState.value = LoadState.ERROR
    }
    val repositoryImpl = PhotoRemoteRepositoryImpl(application)
    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _loadState.value = LoadState.SUCCESS

            _state.value = ProfileState.Success(repositoryImpl.getProfile())

        }

    }


    suspend fun getUserProfile(): MeResponse {

        query.value = repositoryImpl.getProfile().name.toString()
        return repositoryImpl.getProfile()

    }
/*
    fun getPhoto() = userName.asStateFlow()
        .flatMapLatest { photosPagingUseCase.getPhoto(Requester.PROFILE.apply { param = it }) }
        .cachedIn(CoroutineScope(Dispatchers.IO))*/


    fun like(item: Photo) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            //     photoLikeUseCase.likePhoto(item)
            _loadState.value = LoadState.SUCCESS
        }
    }

    fun setUsername(newText: String, refresh: () -> Unit) {
        job?.cancel()
        job = viewModelScope.launch {
            userName.value = newText
            refresh()
        }
    }
}