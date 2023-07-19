package com.example.myapplication.ui.main.responses.saved.comments

import com.google.gson.annotations.SerializedName


data class Gildings (
    @SerializedName("subreddit_id"                    ) var subredditId                  : String?           = null
)