package com.example.myapplication.ui.main.responses.saved.comments

import com.example.myapplication.ui.main.responses.saved.comments.Data
import com.google.gson.annotations.SerializedName


data class SavedCommentsResponse (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var data : Data?   = Data()

)