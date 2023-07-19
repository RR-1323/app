package com.example.myapplication.ui.main.responses.posts

import com.google.gson.annotations.SerializedName


data class Gildings (
    @SerializedName("after"      ) var after     : String?             = null,
)