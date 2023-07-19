package com.example.myapplication.ui.main.Digest

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.myapplication.ui.main.model.Digest
import com.example.myapplication.ui.main.repository.PhotoRemoteRepositoryImpl

class DigestPagingImpl(val application: Application) {

val repository  = PhotoRemoteRepositoryImpl(application)
    fun getDigest():Pager<Int, Digest> {
        Log.d(ContentValues.TAG, "getDigest: ")
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { DigestPagingSource(application) }
        )
    }
}