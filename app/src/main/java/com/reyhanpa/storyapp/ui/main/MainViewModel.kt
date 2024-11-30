package com.reyhanpa.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.reyhanpa.storyapp.data.pref.UserModel
import com.reyhanpa.storyapp.data.remote.response.StoryResponse
import com.reyhanpa.storyapp.repositories.Repository
import com.reyhanpa.storyapp.utils.Event
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _stories = MutableLiveData<StoryResponse>()
    val stories: LiveData<StoryResponse> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getStories()
                _stories.postValue(response)
            } catch (e: Exception) {
                _errorMessage.postValue(Event("Error: ${e.message ?: "Unknown error"}"))
            } finally {
                _isLoading.value = false
            }
        }
    }

}