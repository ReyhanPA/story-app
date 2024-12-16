package com.reyhanpa.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reyhanpa.storyapp.di.Injection
import com.reyhanpa.storyapp.repositories.Repository
import com.reyhanpa.storyapp.ui.auth.AuthViewModel
import com.reyhanpa.storyapp.ui.main.MainViewModel
import com.reyhanpa.storyapp.ui.map.MapViewModel
import com.reyhanpa.storyapp.ui.upload.UploadViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            AuthViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            UploadViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            MapViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @JvmStatic
        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))
    }
}