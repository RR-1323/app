package com.example.myapplication.ui.main.profile

import androidx.paging.PagingData
import com.example.myapplication.ui.main.Photo
import kotlinx.coroutines.flow.Flow

interface PhotosPagingUseCase {

    fun getPhoto(requester: Requester) : Flow<PagingData<Photo>>

}
enum class Requester (var param: String="") {
    ALL_LIST,COLLECTIONS,PROFILE
}