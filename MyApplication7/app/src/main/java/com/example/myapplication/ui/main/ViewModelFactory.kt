package com.example.myapplication.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.myapplication.ui.main.Digest.DigestDetailViewModel
import com.example.myapplication.ui.main.detail.DetailViewModel
import com.example.myapplication.ui.main.profile.ProfileViewModel

class ViewModelFactory(val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {

            MainViewModel::class.java -> MainViewModel(application)
            ProfileViewModel::class.java -> ProfileViewModel(application)
            DetailViewModel::class.java -> DetailViewModel(application)
            DigestDetailViewModel::class.java -> DigestDetailViewModel(application)
            else -> throw java.lang.IllegalArgumentException("Unknown class name")
        }
        return viewModel as T
    }
}