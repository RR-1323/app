package com.example.myapplication.ui.main

import android.app.Application
import com.example.myapplication.ui.main.model.PhotoDetails
import com.example.myapplication.ui.main.repository.PhotoRemoteRepositoryImpl

class LikeDetailPhotoUseCaseImpl(val application: Application) {
    val repository = PhotoRemoteRepositoryImpl(application)
    suspend fun likeDetailPhoto(item: PhotoDetails) {
        if (item.likedByUser) repository.unlikePhoto(item.id)
        else repository.likePhoto(item.id)
    }
}