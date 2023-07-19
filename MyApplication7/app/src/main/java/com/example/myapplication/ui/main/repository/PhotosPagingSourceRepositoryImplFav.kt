package com.example.myapplication.ui.main.repository

import android.app.Application
import androidx.paging.*

import com.example.myapplication.ui.main.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException

class PhotosPagingSourceRepositoryImplFav(val application: Application, val user: String) {




/*
    val localRepository = LocalRepositoryImpl()
    @OptIn(ExperimentalPagingApi::class)
    fun getFlowPhoto(): Flow<PagingData<Photo>>
            = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        remoteMediator = PhotosRemoteMediatorFav(application, photosDao, user),
        pagingSourceFactory = { localRepository.getPagingData() }
    ).flow.map {
        it.map { entity ->
            entity.toPhoto()
        }
    }*/
}