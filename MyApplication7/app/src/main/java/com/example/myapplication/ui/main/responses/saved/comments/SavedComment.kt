package com.example.myapplication.ui.main.responses.saved.comments

import com.example.myapplication.ui.main.responses.saved.comments.CommentData
import com.google.gson.annotations.SerializedName


data class SavedComment (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var commentData : CommentData?   = CommentData()

)