package com.example.myapplication.ui.main.responses.posts.comments

import com.google.gson.annotations.SerializedName


data class Gildings (
    @SerializedName("kind" ) var kind : String? = null,
)