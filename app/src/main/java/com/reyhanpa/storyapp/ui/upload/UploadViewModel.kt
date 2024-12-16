package com.reyhanpa.storyapp.ui.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.reyhanpa.storyapp.BuildConfig
import com.reyhanpa.storyapp.data.remote.response.ErrorResponse
import com.reyhanpa.storyapp.data.remote.response.UploadResponse
import com.reyhanpa.storyapp.repositories.Repository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UploadViewModel(private val repository: Repository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadImage(
        imageFile: File,
        description: String,
        lat: Double? = null,
        lon: Double? = null
    ): LiveData<UploadResponse> {
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val latBody = lat?.toString()?.toRequestBody("text/plain".toMediaType())
        val lonBody = lon?.toString()?.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        _isLoading.value = true
        val liveDataResponse = MutableLiveData<UploadResponse>()
        viewModelScope.launch {
            try {
                val uploadSuccessResponse = repository.uploadImage(multipartBody, requestBody, latBody, lonBody)
                liveDataResponse.postValue(uploadSuccessResponse)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val uploadFailResponse = Gson().fromJson(jsonInString, UploadResponse::class.java)
                val uploadFailMessage = Gson().fromJson(jsonInString, ErrorResponse::class.java).message
                liveDataResponse.postValue(uploadFailResponse)
                if (BuildConfig.DEBUG) Log.e(TAG, "onFailure: $uploadFailMessage")
            }
            _isLoading.value = false
        }
        return liveDataResponse
    }

    companion object{
        private const val TAG = "UploadViewModel"
    }
}