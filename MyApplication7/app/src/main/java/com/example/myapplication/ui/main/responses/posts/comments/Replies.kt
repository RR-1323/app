package com.example.myapplication.ui.main.responses.posts.comments

import com.example.myapplication.ui.main.responses.posts.comments.Data
import com.google.gson.annotations.SerializedName


data class Replies (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var data : Data?   = Data()

)