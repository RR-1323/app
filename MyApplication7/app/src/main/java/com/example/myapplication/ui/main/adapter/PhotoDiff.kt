package com.example.myapplication.ui.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.ui.main.Photo


class PhotoDiff : DiffUtil.ItemCallback<Photo>() {

    override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
        oldItem == newItem
}