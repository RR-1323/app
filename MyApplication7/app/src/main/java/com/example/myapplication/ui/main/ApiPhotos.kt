package com.example.myapplication.ui.main


import android.content.Context
import android.util.Log
import com.example.myapplication.ui.main.model.PhotoDetails
import com.example.myapplication.ui.main.model.Profile

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiPhotos {

    @GET("photos/?client_id=6V5MrkH8_pUaxqLiS0he3hQqzdywrlzHPNmaVcv1E30")
    suspend fun getPhotos(@Query("page") page: Int): photolistnew
    @GET("photos/{id}?client_id=6V5MrkH8_pUaxqLiS0he3hQqzdywrlzHPNmaVcv1E30")
    suspend fun getPhotoDetails(@Path("id") id: String): PhotoDetails

    @GET("me")
    suspend fun getProfile(): Profile

}


class ResponseToken(
    val access_token: String
)

val  okhttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().also {
    it.level = HttpLoggingInterceptor.Level.BODY
}).build()


val retrofit = Retrofit.Builder()
    .baseUrl("https://api.unsplash.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(okhttpClient)
    .build()
    .create(ApiPhotos::class.java)



