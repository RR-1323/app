package com.example.myapplication.ui.main.adapter

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myapplication.R
import com.example.myapplication.databinding.PhotoViewHolderBinding
import com.example.myapplication.ui.main.Photo
import com.example.myapplication.ui.main.state.ClickableView

class PhotoViewHolder(private val binding: PhotoViewHolderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Photo, onClick: (ClickableView, item: Photo) -> Unit) {

        binding.photo.setOnClickListener {
            onClick(ClickableView.PHOTO, item)
        }
//        binding.isLiked.setOnClickListener {
//            onClick(ClickableView.LIKE, item)
//        }

        binding.currentLikes.text = item.likes.toString()
        //  binding.isLiked.isSelected = item.likedByUser
        //  binding.isLiked.isActivated =item.likedByUser
        binding.photo.loadImage(item.urlsSmall)
        binding.authorAvatar.loadImage(item.userAvatar)

        binding.authorName.text = item.userName
    }
}

fun ImageView.loadImage(urls: String?) {
    Glide.with(this)
        .load(urls)
        .error(R.drawable.error_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.placeholder)
        .into(this)
}


