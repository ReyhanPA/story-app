package com.reyhanpa.storyapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.reyhanpa.storyapp.data.local.mediator.StoryRemoteMediator
import com.reyhanpa.storyapp.data.local.room.StoryDatabase
import com.reyhanpa.storyapp.data.pref.UserModel
import com.reyhanpa.storyapp.data.pref.UserPreference
import com.reyhanpa.storyapp.data.remote.response.ListStoryItem
import com.reyhanpa.storyapp.data.remote.response.LoginResponse
import com.reyhanpa.storyapp.data.remote.response.RegisterResponse
import com.reyhanpa.storyapp.data.remote.response.StoryResponse
import com.reyhanpa.storyapp.data.remote.response.UploadResponse
import com.reyhanpa.storyapp.data.remote.retrofit.ApiService
import com.reyhanpa.storyapp.utils.toStoryItem
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository private constructor(
    private val storyDatabase: StoryDatabase,
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
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
//                StoryPagingSource(apiService)
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData.map { pagingData ->
            pagingData.map { listStoryEntity ->
                listStoryEntity.toStoryItem()
            }
        }

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
        fun getInstance(storyDatabase: StoryDatabase, userPref: UserPreference, apiService: ApiService) = Repository(storyDatabase, userPref, apiService)
    }
}