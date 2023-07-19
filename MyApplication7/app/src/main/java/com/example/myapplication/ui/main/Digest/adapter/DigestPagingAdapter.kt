package com.example.myapplication.ui.main.Digest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.myapplication.databinding.DigestViewHolderBinding
import com.example.myapplication.ui.main.model.Digest


class DigestPagingAdapter(
    private val onClick: (Digest) -> Unit,
) : PagingDataAdapter<Digest, DigestViewHolder>(DigestDiff()) {

    override fun onBindViewHolder(holder: DigestViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item) { digest -> onClick(digest) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DigestViewHolder(
        DigestViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}