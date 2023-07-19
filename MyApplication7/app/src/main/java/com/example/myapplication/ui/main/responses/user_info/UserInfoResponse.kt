package com.example.myapplication.ui.main.responses.user_info

import com.example.myapplication.ui.main.responses.user_info.UserInfo
import com.google.gson.annotations.SerializedName


data class UserInfoResponse (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("data" ) var data : UserInfo?   = UserInfo()

)