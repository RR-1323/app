package com.example.myapplication.ui.main.responses.saved.links

import com.example.myapplication.ui.main.responses.saved.links.Data
import com.google.gson.annotations.SerializedName


data class SavedLinksResponse (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var data : Data?   = Data()

)