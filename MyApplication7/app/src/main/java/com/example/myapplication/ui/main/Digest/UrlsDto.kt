package com.example.myapplication.ui.main.Digest

import com.google.gson.annotations.SerializedName


data class UrlsDto(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
    @SerializedName("small_s3")
    val smallS3: String
)