package com.example.myapplication.ui.main.profile

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.myapplication.PhotoEntity
import com.example.myapplication.ui.main.repository.LocalRepository
import com.example.myapplication.ui.main.repository.PhotoRemoteRepositoryImpl

@OptIn(ExperimentalPagingApi::class)
class PhotosRemoteMed (
    private val localRepository: LocalRepository,
    private val photoRemoteRepository: PhotoRemoteRepositoryImpl,
    private val requester: Requester
) : RemoteMediator<Int, PhotoEntity>() {
    /*
    private var pageIndex = 0

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>,
    ): MediatorResult {
        pageIndex = getIndex(loadType) ?: return MediatorResult.Success(true)

        return try {
            val response = photoRemoteRepository.getPhotoListLast(requester, pageIndex).toListEntity()
            if (loadType == LoadType.REFRESH) localRepository.refresh(response)
            else localRepository.insertData(response)
            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private fun getIndex(loadType: LoadType): Int? {
        return when (loadType) {
            LoadType.PREPEND -> null
            LoadType.REFRESH -> 0
            LoadType.APPEND -> ++pageIndex /*поменять на null чтобы грузить 1 страницу*/
        }
    }*/
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): MediatorResult {
        TODO("Not yet implemented")
    }
}