package com.reyhanpa.storyapp.repositories

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.reyhanpa.storyapp.data.pref.UserModel
import com.reyhanpa.storyapp.data.pref.UserPreference
import com.reyhanpa.storyapp.data.remote.pagination.StoryPagingSource
import com.reyhanpa.storyapp.data.remote.response.ListStoryItem
import com.reyhanpa.storyapp.data.remote.response.LoginResponse
import com.reyhanpa.storyapp.data.remote.response.RegisterResponse
import com.reyhanpa.storyapp.data.remote.response.StoryResponse
import com.reyhanpa.storyapp.data.remote.response.UploadResponse
import com.reyhanpa.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun getStoriesWithLocation(): StoryResponse {
        return apiService.getStoriesWithLocation()
    }

    suspend fun uploadImage(multipartBody: MultipartBody.Part, description: RequestBody): UploadResponse {
        return apiService.uploadImage(multipartBody, description)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(userPref: UserPreference, apiService: ApiService) = Repository(userPref, apiService)
    }
}