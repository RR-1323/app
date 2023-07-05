package com.example.myapplication.ui.main.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.myapplication.ui.main.ResponseToken
import com.example.myapplication.ui.main.model.PhotoDetails
import com.example.myapplication.ui.main.model.Profile
import com.example.myapplication.ui.main.photolistnew
import com.example.myapplication.ui.main.retrofit



class PhotoRemoteRepositoryImpl (


) {

    suspend fun getPhotoList(page: Int): photolistnew {
        Log.d(TAG, "getPhotoList:")
      return  retrofit.getPhotos(page)
    }


    suspend fun getPhotoDetail(id: String): PhotoDetails {
        Log.d(TAG, "getPhotoDetails:")
        return retrofit.getPhotoDetails(id)
    }

    suspend fun getProfile(): Profile {
        Log.d(TAG, "getProfile:")
        return retrofit.getProfile()
    }


}