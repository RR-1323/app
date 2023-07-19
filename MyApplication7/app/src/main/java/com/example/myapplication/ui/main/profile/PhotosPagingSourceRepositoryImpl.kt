package com.example.myapplication.ui.main.profile

import androidx.paging.*
import com.example.myapplication.PhotoEntity
import com.example.myapplication.ui.main.Photo
import com.example.myapplication.ui.main.WrapperPhotoDto
import com.example.myapplication.ui.main.repository.LocalRepository
import com.example.myapplication.ui.main.repository.PhotoRemoteRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PhotosPagingSourceRepositoryImpl (
    private val photoRemoteRepository: PhotoRemoteRepositoryImpl,
    private val localRepository: LocalRepository
): PhotosPagingSourceRepository {

/*
    @OptIn(ExperimentalPagingApi::class)
    override fun getFlowPhoto(requester: Requester): Flow<PagingData<Photo>>
            = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        remoteMediator = PhotosRemoteMed(localRepository, photoRemoteRepository, requester),
        pagingSourceFactory = { localRepository.getPagingData() }
    ).flow.map {
        it.map { entity ->
            entity.toPhoto()
        }
    }

    override suspend fun setLike(id: String): WrapperPhotoDto = photoRemoteRepository.likeLast(id)

    override suspend fun deleteLike(id: String): WrapperPhotoDto = photoRemoteRepository.unlikeLast  (id)

    override suspend fun updateLikeDB(entity: PhotoEntity) = localRepository.setLikeInDataBase(entity)*/
}