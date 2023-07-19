package com.example.myapplication.ui.main


import com.example.myapplication.PhotoDetailsDto
import com.example.myapplication.PhotoListDto
import com.example.myapplication.ui.main.auth.interceptor.AuthTokenInterceptor
import com.example.myapplication.ui.main.model.DigestDto
import com.example.myapplication.ui.main.model.DigestListDto
import com.example.myapplication.ui.main.state.SearchDto
import com.example.myapplication.ui.main.profileDto.ProfileDto
import com.example.myapplication.ui.main.responses.friends.FriendsResponse
import com.example.myapplication.ui.main.responses.me.MeResponse
import com.example.myapplication.ui.main.responses.user_info.UserInfoResponse

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiPhotos {

    @GET("photos")
    suspend fun getPhotos(@Query("page") page: Int): PhotoListDto


    @GET("photos/{id}")
    suspend fun getPhotoDetails(@Path("id") id: String): PhotoDetailsDto



        @GET("api/v1/me")
        suspend fun me(): MeResponse
        @GET("/api/v1/me/friends")
        suspend fun friendsList(@Header("Authorization") authHeader:String): FriendsResponse
        @PUT("/api/v1/me/friends/{username}")
        suspend fun addToFriends(@Path("username") username:String,@Body requestBody: String,@Header("Authorization") authHeader:String)
        @GET("user/{username}/about")
        suspend fun user(@Path("username") userName: String,@Header("Authorization") authHeader:String): UserInfoResponse


    @GET("users/{username}/likes")
    suspend fun getProfileLikes(
        @Path("username") userName: String,
        @Query("page") page: Int
    ): PhotoListDto

    @POST("photos/{id}/like")
    suspend fun like(@Path("id") id: String): WrapperPhotoDto


    @DELETE("photos/{id}/like")
    suspend fun unlike(@Path("id") id: String): WrapperPhotoDto

    @GET("search/photos")
    suspend fun searchPhotos(@Query("query") query: String, @Query("page") page: Int): SearchDto


    @GET("users/{username}/likes")
    suspend fun getLikedPhotos(
        @Path("username") userName: String,
        @Query("page") page: Int
    ): PhotoListDto

    @POST("photos/{id}/like")
    suspend fun likeLast(@Path("id") id: String): WrapperPhotoDto

    @DELETE("photos/{id}/like")
    suspend fun unlikeLast(@Path("id") id: String): WrapperPhotoDto


    @GET("collections")
    suspend fun getDigests(@Query("page") page: Int): DigestListDto

    @GET("collections/{id}/photos")
    suspend fun getDigestPhotos(
        @Path("id") id: String,
        @Query("page") page: Int
    ): PhotoListDto


    @GET("collections/{id}")
    suspend fun getDigestInfo(
        @Path("id") id: String
    ): DigestDto
}


fun getRetrofitThis(): ApiPhotos = Retrofit.Builder()

    .baseUrl("https://oauth.reddit.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(
        OkHttpClient.Builder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BASIC)
            )
            .addInterceptor(AuthTokenInterceptor())
            .followRedirects(true)
            .build()
    )
    .build()
    .create(ApiPhotos::class.java)



