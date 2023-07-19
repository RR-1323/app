package com.example.myapplication.ui.main.responses.friends

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("children" ) var friends : ArrayList<Friend> = arrayListOf()

)