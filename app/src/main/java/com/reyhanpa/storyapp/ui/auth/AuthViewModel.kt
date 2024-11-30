package com.reyhanpa.storyapp.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.reyhanpa.storyapp.data.pref.UserModel
import com.reyhanpa.storyapp.data.remote.response.ErrorResponse
import com.reyhanpa.storyapp.data.remote.response.LoginResponse
import com.reyhanpa.storyapp.data.remote.response.RegisterResponse
import com.reyhanpa.storyapp.repositories.Repository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel(private val repository: Repository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun register(name: String, email: String, password: String): LiveData<RegisterResponse> {
        _isLoading.value = true
        val liveDataResponse = MutableLiveData<RegisterResponse>()
        viewModelScope.launch {
            try {
                val registerSuccessResponse = repository.register(name, email, password)
                liveDataResponse.postValue(registerSuccessResponse)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val registerFailResponse = Gson().fromJson(jsonInString, RegisterResponse::class.java)
                val registerFailMessage = Gson().fromJson(jsonInString, ErrorResponse::class.java).message
                liveDataResponse.postValue(registerFailResponse)
                Log.e(TAG, "onFailure: $registerFailMessage")
            }
            _isLoading.value = false
        }
        return liveDataResponse
    }

    fun login(email: String, password: String): LiveData<LoginResponse> {
        _isLoading.value = true
        val liveDataResponse = MutableLiveData<LoginResponse>()
        viewModelScope.launch {
            try {
                val loginSuccessResponse = repository.login(email, password)
                liveDataResponse.postValue(loginSuccessResponse)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val loginFailResponse = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val loginFailMessage = Gson().fromJson(jsonInString, ErrorResponse::class.java).message
                liveDataResponse.postValue(loginFailResponse)
                Log.e(TAG, "onFailure: $loginFailMessage")
            }
            _isLoading.value = false
        }
        return liveDataResponse
    }

    companion object{
        private const val TAG = "AuthViewModel"
    }
}