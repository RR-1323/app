package com.example.myapplication.ui.main.repository

import androidx.paging.*
import com.example.myapplication.ui.main.photolistnew


import okio.IOException
import retrofit2.HttpException


class PhotosPagingSourceRepositoryImpl: PagingSource<Int, photolistnew.photoNewItem>() {
private val photoRemoteRepository = PhotoRemoteRepositoryImpl()





    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, photolistnew.photoNewItem> {
        val page = params.key ?: 1
        return try {
            val response = photoRemoteRepository.getPhotoList(page)
            PagingSource.LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch
            (exception: IOException) {

            return LoadResult.Error(exception)
        } catch (exception: HttpException) {

            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, photolistnew.photoNewItem>): Int? = 1
}