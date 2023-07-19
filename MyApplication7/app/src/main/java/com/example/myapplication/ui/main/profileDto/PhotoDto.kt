package com.example.myapplication.ui.main

import com.example.myapplication.PhotoEntity
import com.google.gson.annotations.SerializedName

data class PhotoDto(
    val id: String,
    val urls: UrlsDto,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean,
    val likes: Int,
    val links: LinksDto,
    val user: UserDto,
    val height: Int,
    val width: Int
) {

    fun toPhoto() = Photo(
        id = id,
        urlsSmall = urls.small,
        likedByUser = likedByUser,
        likes = likes,
        userName = user.name,
        userAvatar = user.profileImage.small,
        height = height,
        width = width
    )

    fun toPhotoEntity() = PhotoEntity(
        photoId = id,
        smallUrls = urls.small,
        likedByUser = likedByUser,
        counterLikes = likes,
        userName = user.name,
        profileImage = user.profileImage.small
    )
}

data class UrlsDto(
    val full: String,
    val raw: String,
    val regular: String,
    val small: String,
    val smallS3: String,
    val thumb: String
) {
    fun toPhotoDetailsUrls() = Urls(raw, regular)
}

data class LinksDto(
    val download: String,
    val downloadLocation: String,
    val html: String,
    val self: String
)

data class UserDto(
    val bio: String?,
    val name: String,
    @SerializedName("profile_image")
    val profileImage: ProfileImageDto,
    val username: String
) {
    fun toPhotoDetailsUser() = User(bio, name, profileImage.toPhotoDetailsProfileImage(), username)
}

data class ProfileImageDto(
    val large: String,
    val medium: String,
    val small: String
) {
    fun toPhotoDetailsProfileImage() = ProfileImage(small)
}

data class ProfileImage(
    val small: String
)

data class User(
    val bio: String?,
    val name: String,
    @SerializedName("profile_image")
    val profileImage: ProfileImage,
    val username: String
)

