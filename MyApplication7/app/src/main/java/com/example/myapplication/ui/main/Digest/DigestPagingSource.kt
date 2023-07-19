package com.example.myapplication.ui.main.Digest

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.ui.main.model.Digest
import com.example.myapplication.ui.main.repository.PhotoRemoteRepositoryImpl

class DigestPagingSource(val application: Application) :
    PagingSource<Int, Digest>() {

    val repository = PhotoRemoteRepositoryImpl(application)


    override fun getRefreshKey(state: PagingState<Int, Digest>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Digest> {
        Log.d(ContentValues.TAG, "load: ")
        val page = params.key ?: FIRST_PAGE
        Log.i(ContentValues.TAG, "page: $page")
        return kotlin.runCatching {
            repository.getDigests(page)
        }.fold(
            onSuccess = {
                Log.i(ContentValues.TAG, "Processing")
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )

            }, onFailure = {
                Log.i(ContentValues.TAG, "Failure")
                LoadResult.Error(it)
            }
        )
    }

    private companion object {
        private const val FIRST_PAGE = 1
    }
}
