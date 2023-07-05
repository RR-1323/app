package com.example.myapplication.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.ui.main.repository.PhotosPagingSourceRepositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {


    val _throwable = MutableLiveData<Throwable?>(null)
    val throwable: LiveData<Throwable?> = _throwable



    var _isLoading = MutableStateFlow(false)
    //  val filterEnabled = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val pagedMovies: Flow<PagingData<photolistnew.photoNewItem>> = Pager (
        config = PagingConfig(pageSize = 10 ),
        pagingSourceFactory = { PhotosPagingSourceRepositoryImpl() }
    ).flow.cachedIn(viewModelScope)


}

