package com.example.myapplication.ui.main.repository

import android.app.Application
import androidx.paging.*
import com.example.myapplication.PhotoEntity

import com.example.myapplication.ui.main.Photo
import com.example.myapplication.ui.main.WrapperPhotoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


import okio.IOException
import retrofit2.HttpException


class PhotosPagingSourceRepositoryImpl(val application: Application) {

/*
val photoRemoteRepository = PhotoRemoteRepositoryImpl(application)
    val localRepository = LocalRepositoryImpl()
 @OptIn(ExperimentalPagingApi::class)
 fun getFlowPhoto(): Flow<PagingData<Photo>>
            = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        remoteMediator = PhotosRemoteMediator(application),
        pagingSourceFactory = { localRepository.getPagingData() }
    ).flow.map {
        it.map { entity ->
            entity.toPhoto()
        }
    }

*/
}