package com.example.myapplication.ui.main.Digest.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.DigestViewHolderBinding
import com.example.myapplication.ui.main.adapter.loadImage
import com.example.myapplication.ui.main.model.Digest


class DigestViewHolder(private val binding: DigestViewHolderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Digest, onClick: (item: Digest) -> Unit) {

        binding.root.setOnClickListener {
            onClick(item)
        }

        binding.preview.loadImage(item.previewPhoto)
        binding.authorAvatar.loadImage(item.userProfileImage)
        binding.authorName.text = item.userUsername
        binding.totalPhotos.text =
            itemView.context.resources.getQuantityString(
                R.plurals.total_photos, item.totalPhotos, item.totalPhotos
            )
        binding.collectionTitle.text = item.title

    }
}

