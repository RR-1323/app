package com.example.myapplication.ui.main.responses.saved.links

import com.example.myapplication.ui.main.responses.saved.links.Images
import com.google.gson.annotations.SerializedName


data class Preview (

    @SerializedName("images"  ) var images  : ArrayList<Images> = arrayListOf(),
    @SerializedName("enabled" ) var enabled : Boolean?          = null

)