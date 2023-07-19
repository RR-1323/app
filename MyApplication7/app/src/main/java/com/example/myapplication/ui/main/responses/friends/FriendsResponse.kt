package com.example.myapplication.ui.main.responses.friends

import com.example.myapplication.ui.main.responses.friends.Data
import com.google.gson.annotations.SerializedName


data class FriendsResponse (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var data : Data?   = Data()

)