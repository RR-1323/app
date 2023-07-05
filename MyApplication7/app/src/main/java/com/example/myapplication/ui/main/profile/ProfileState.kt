package com.example.myapplication.ui.main.profile

import com.example.myapplication.ui.main.model.Profile

sealed class ProfileState{
    data class Success(val data: Profile): ProfileState()
    object NotStartedYet : ProfileState()
}
