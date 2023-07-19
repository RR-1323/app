package com.example.myapplication

import com.example.myapplication.ui.main.PhotoDto

class PhotoListDto : ArrayList<PhotoDto>() {

    fun toListEntity(): MutableList<PhotoEntity> {
        val newList = mutableListOf<PhotoEntity>()
        this.forEach {
            newList.add(it.toPhotoEntity())
        }
        return newList
    }
}