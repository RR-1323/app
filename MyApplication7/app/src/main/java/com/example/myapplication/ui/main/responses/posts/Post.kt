package com.example.myapplication.ui.main.responses.posts

import com.google.gson.annotations.SerializedName


data class Post (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var data : PostData?   = PostData()

)