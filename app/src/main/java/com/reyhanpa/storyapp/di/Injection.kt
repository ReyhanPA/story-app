package com.reyhanpa.storyapp.di

import android.content.Context
import com.reyhanpa.storyapp.data.pref.UserPreference
import com.reyhanpa.storyapp.data.pref.dataStore
import com.reyhanpa.storyapp.data.remote.retrofit.ApiConfig
import com.reyhanpa.storyapp.repositories.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }
}