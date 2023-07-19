package com.example.myapplication.ui.main.profile

import androidx.paging.PagingData
import com.example.myapplication.ui.main.Photo
import kotlinx.coroutines.flow.Flow

class PhotosPagingUseCaseImpl (
    private val photosRepository: PhotosPagingSourceRepository
): PhotosPagingUseCase {
    /*  override fun getPhoto(requester: Requester)=
        photosRepository.getFlowPhoto(requester)
*/
    override fun getPhoto(requester: Requester): Flow<PagingData<Photo>> {
        TODO("Not yet implemented")
    }
}
