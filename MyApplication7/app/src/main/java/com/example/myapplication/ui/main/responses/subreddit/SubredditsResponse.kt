package com.example.myapplication.ui.main.responses.subreddit

import com.example.myapplication.ui.main.responses.subreddit.Data
import com.google.gson.annotations.SerializedName


data class SubredditsResponse (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var data : Data?   = Data()

)