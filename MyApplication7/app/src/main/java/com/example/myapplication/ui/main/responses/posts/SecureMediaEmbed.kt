package com.example.myapplication.ui.main.responses.posts

import com.google.gson.annotations.SerializedName


data class SecureMediaEmbed (
    @SerializedName("after"      ) var after     : String?             = null,
)