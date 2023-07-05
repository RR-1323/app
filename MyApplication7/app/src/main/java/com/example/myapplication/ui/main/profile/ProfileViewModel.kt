package com.example.myapplication.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.main.model.Profile
import com.example.myapplication.ui.main.photolistnew
import com.example.myapplication.ui.main.repository.PhotoRemoteRepositoryImpl
import com.example.myapplication.ui.main.state.LoadState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ProfileViewModel : ViewModel() {

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
val repositoryImpl = PhotoRemoteRepositoryImpl()
    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _loadState.value = LoadState.SUCCESS
            _state.value = ProfileState.Success(repositoryImpl.getProfile())
        }
    }
/*
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getPhoto() = userName.asStateFlow()
        .flatMapLatest { repositoryImpl.getPhoto(Requester.PROFILE.apply { param = it })}
        .cachedIn(CoroutineScope(Dispatchers.IO))
*/
    fun like(item: photolistnew.photoNewItem) {
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