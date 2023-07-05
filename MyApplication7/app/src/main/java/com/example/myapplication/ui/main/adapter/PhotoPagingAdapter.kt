package com.example.myapplication.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.myapplication.databinding.PhotoViewHolderBinding
import com.example.myapplication.ui.main.photolistnew

import com.example.myapplication.ui.main.state.ClickableView


class PhotoPagingAdapter(
    private val onClick: (ClickableView, photolistnew.photoNewItem) -> Unit
) : PagingDataAdapter<photolistnew.photoNewItem, PhotoViewHolder>(PhotoDiff()) {

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item) { ClickableView, Photo ->
                onClick(ClickableView, Photo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PhotoViewHolder(
        PhotoViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}