package com.reyhanpa.storyapp.di

import android.content.Context
import com.reyhanpa.storyapp.data.pref.UserPreference
import com.reyhanpa.storyapp.data.pref.dataStore
import com.reyhanpa.storyapp.data.remote.retrofit.ApiConfig
import com.reyhanpa.storyapp.repositories.Repository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return Repository.getInstance(pref, apiService)
    }
}