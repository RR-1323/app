package com.example.myapplication.ui.main.responses.posts.comments

import com.example.myapplication.ui.main.responses.saved.comments.CommentData
import com.google.gson.annotations.SerializedName


data class Comment (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var data : CommentData?   = CommentData()

)