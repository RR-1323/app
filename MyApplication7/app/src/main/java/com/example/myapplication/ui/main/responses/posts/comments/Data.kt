package com.example.myapplication.ui.main.responses.posts.comments

import com.example.myapplication.ui.main.responses.posts.comments.Comment
import com.google.gson.annotations.SerializedName

data class Data (
    @SerializedName("after"      ) var after     : String?             = null,
    @SerializedName("dist"       ) var dist      : String?             = null,
    @SerializedName("modhash"    ) var modhash   : String?             = null,
    @SerializedName("geo_filter" ) var geoFilter : String?             = null,
    @SerializedName("children"   ) var children  : ArrayList<Comment> = arrayListOf(),
    @SerializedName("before"     ) var before    : String?             = null
        )