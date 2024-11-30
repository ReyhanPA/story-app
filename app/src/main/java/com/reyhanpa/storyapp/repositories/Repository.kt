package com.reyhanpa.storyapp.repositories

import com.reyhanpa.storyapp.data.pref.UserModel
import com.reyhanpa.storyapp.data.pref.UserPreference
import com.reyhanpa.storyapp.data.remote.response.LoginResponse
import com.reyhanpa.storyapp.data.remote.response.RegisterResponse
import com.reyhanpa.storyapp.data.remote.response.StoryResponse
import com.reyhanpa.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun getStories(): StoryResponse {
        return apiService.getStories()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
    }
}