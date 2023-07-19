package com.example.myapplication.ui.main.model

data class DigestTagDto(
    val title: String
) {

    fun toDigestTag() = DigestTag(title)
}
