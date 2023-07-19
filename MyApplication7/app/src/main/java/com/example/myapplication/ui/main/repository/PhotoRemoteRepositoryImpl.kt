package com.example.myapplication.ui.main.repository

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.example.myapplication.PhotoListDto
import com.example.myapplication.ui.main.Photo
import com.example.myapplication.ui.main.WrapperPhotoDto
import com.example.myapplication.ui.main.getRetrofitThis
import com.example.myapplication.ui.main.model.Digest
import com.example.myapplication.ui.main.model.DigestDto
import com.example.myapplication.ui.main.model.PhotoDetails
import com.example.myapplication.ui.main.profile.Requester
import com.example.myapplication.ui.main.responses.me.MeResponse


class PhotoRemoteRepositoryImpl(private val application: Application) {

//    suspend fun getPhotoList(page: Int): photolistnew {
//        Log.d(TAG, "getPhotoList:")
//      return  retrofit.getPhotos(page)
//    }

    suspend fun getPhotoListLast(requester: Requester, page: Int): PhotoListDto {
        Log.d(ContentValues.TAG, "getPhotoList: $requester")
        return when (requester) {
            Requester.ALL_LIST -> checkRequesters(requester.param, page)
            Requester.COLLECTIONS -> getRetrofitThis().getDigestPhotos(requester.param, page)
            Requester.PROFILE -> getRetrofitThis().getProfileLikes(requester.param, page)
        }
    }

    private suspend fun checkRequesters(query: String, page: Int) =
        if (query == "") getRetrofitThis().getPhotos(page)
        else getRetrofitThis().searchPhotos(query, page).results


    suspend fun getPhotoDetail(id: String): PhotoDetails {
        val toPhotoDetails = getRetrofitThis().getPhotoDetails(id).toPhotoDetails()
        Log.d(TAG, "getPhotoDetails: $toPhotoDetails")
        return toPhotoDetails
    }

    suspend fun getProfile(): MeResponse {
        val me = getRetrofitThis().me()
        Log.d(TAG, "getProfile: $me")
        return me
    }


    suspend fun getLikedPhotos(user: String, page: Int): PhotoListDto {
        Log.d(TAG, "getPhotoList:")
        return  getRetrofitThis().getLikedPhotos(user, page)
    }



suspend fun getLikedPhotoList(user: String, page: Int): PhotoListDto {
    return  getRetrofitThis().getLikedPhotos  (user,page)
}


    suspend fun likeDetailPhotoRepo(item: PhotoDetails) {
        if (item.likedByUser) likePhoto(item.id)
        else unlikePhoto(item.id)
    }
    suspend fun checkRequester(/*query: String, */page: Int): PhotoListDto =
      /*  if (query == "")*/  getRetrofitThis().getPhotos(page)
       // else getRetrofitThis().searchPhotos(query, page).results


    suspend fun getDigests(page: Int): List<Digest> =
        getRetrofitThis().getDigests(page).toListDigest()


    suspend fun likeLast(id: String): WrapperPhotoDto = getRetrofitThis().likeLast(id)


    suspend fun unlikeLast(id: String) : WrapperPhotoDto= getRetrofitThis().unlikeLast(id)

  suspend fun getDigestInfo(id: String): Digest = getRetrofitThis().getDigestInfo(id).toDigest()

   suspend fun likePhoto(id: String):Photo = getRetrofitThis().like(id).photo.toPhoto()

    suspend fun unlikePhoto(id: String): Photo = getRetrofitThis().unlike(id).photo.toPhoto()
    fun List<DigestDto>.toListDigest(): List<Digest> {
        val newList = mutableListOf<Digest>()

        this.forEach { item ->
            newList.add(item.toDigest())
        }
        return newList
    }

}
