package com.example.myapplication.ui.main.profile

import com.example.myapplication.ui.main.responses.me.MeResponse


sealed class ProfileState{
    data class Success(val data: MeResponse): ProfileState()
    object NotStartedYet : ProfileState()
}
