package com.reyhanpa.storyapp.di

import android.content.Context
import com.reyhanpa.storyapp.data.pref.UserPreference
import com.reyhanpa.storyapp.data.pref.dataStore
import com.reyhanpa.storyapp.repositories.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}