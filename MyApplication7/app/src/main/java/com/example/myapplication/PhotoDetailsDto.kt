package com.example.myapplication

import com.example.myapplication.ui.main.ProfileImageDto
import com.example.myapplication.ui.main.model.*
import com.google.gson.annotations.SerializedName

data class PhotoDetailsDto(
    val id: String,
    val downloads: Int,
    val likes: Int,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean,
    val exif: ExifDto,
    val location: LocationDto,
    val tags: List<TagDto>,
    val urls: UrlsDto,
    val links: LinksDto,
    val user: UserDto
) {
    fun toPhotoDetails() = PhotoDetails(
        id = id,
        downloads = downloads,
        likedByUser = likedByUser,
        likes = likes,
        exif = exif.toPhotoDetailsExif(),
        location = location.toPhotoDetailsLocation(),
        tags = tags.toListTag(),
        urls = urls.toPhotoDetailsUrls(),
        user = user.toPhotoDetailsUser()
    )
}

data class LocationDto(
    val city: String?,
    val country: String?,
    val position: PositionDto
) {
    fun toPhotoDetailsLocation() = Location(city, position.toPhotoDetailsPosition())
}

data class PositionDto(
    val latitude: Double?,
    val longitude: Double?
) {
    fun toPhotoDetailsPosition() = Position(latitude, longitude)
}


data class ExifDto(
    val make: String?,
    val model: String?,
    val name: String?,
    val exposureTime: String?,
    val aperture: String?,
    val focalLength: String?,
    val iso: Int?
) {
    fun toPhotoDetailsExif() = Exif(make, model, model, exposureTime, aperture, focalLength, iso)
}

data class TagDto(
    val title: String?
) {
    fun toPhotoDetailsTags() = Tags(title)
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

fun List<TagDto>.toListTag(): List<Tags> {
    return this.map { item -> item.toPhotoDetailsTags() }
}
