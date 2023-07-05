package com.example.myapplication.ui.main.detail

import com.example.myapplication.ui.main.model.PhotoDetails


sealed class DetailsState {
    data class Success(val data: PhotoDetails): DetailsState()
    object NotStartedYet : DetailsState()
}
