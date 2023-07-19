package com.example.myapplication.ui.main.responses.posts

import com.example.myapplication.ui.main.responses.posts.Data
import com.google.gson.annotations.SerializedName


data class SubredditPostsResponse (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var data : Data?   = Data()

)