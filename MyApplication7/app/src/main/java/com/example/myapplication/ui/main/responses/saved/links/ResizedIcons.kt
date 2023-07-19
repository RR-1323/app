package com.example.myapplication.ui.main.responses.saved.links

import com.google.gson.annotations.SerializedName


data class ResizedIcons (

  @SerializedName("url"    ) var url    : String? = null,
  @SerializedName("width"  ) var width  : Int?    = null,
  @SerializedName("height" ) var height : Int?    = null

)