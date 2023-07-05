package com.example.myapplication.ui.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.ui.main.photolistnew


class PhotoDiff : DiffUtil.ItemCallback<photolistnew.photoNewItem>() {

    override fun areItemsTheSame(oldItem: photolistnew.photoNewItem, newItem: photolistnew.photoNewItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: photolistnew.photoNewItem, newItem: photolistnew.photoNewItem) =
        oldItem == newItem
}