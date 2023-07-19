package com.example.myapplication.ui.main.responses.saved.links

import com.example.myapplication.ui.main.responses.saved.links.LinkData
import com.google.gson.annotations.SerializedName


data class SavedLink (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var linkData : LinkData?   = LinkData()

)