package com.example.githubuser.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.model.User
import com.example.githubuser.data.network.repository.UserRepository
import com.example.githubuser.helper.ResourcesHelper
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowViewModel(
    private val userRepository: UserRepository,
    private val usersLoginFromArgs: String,
    private val resourcesHelper: ResourcesHelper
) : ViewModel() {

    private val _followers = MutableLiveData<List<User>>()
    val followers: LiveData<List<User>>
        get() = _followers

    private val _following = MutableLiveData<List<User>>()
    val following: LiveData<List<User>>
        get() = _following

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val _showRetry = MutableLiveData<Boolean>()
    val showRetry: LiveData<Boolean>
        get() = _showRetry

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private fun loadFollowers() {
        _showLoading.value = true
        _showRetry.value = false
        viewModelScope.launch(Dispatchers.IO) {
            when (val followersData = userRepository.getUserFollowers(usersLoginFromArgs)) {
                is NetworkResponse.Success -> {
                    _followers.postValue(followersData.body)
                    _showLoading.postValue(false)
                    _showRetry.postValue(false)
                }
                is NetworkResponse.ServerError -> {
                    _errorMessage.postValue(followersData.body?.message.toString())
                    _showLoading.postValue(false)
                    _showRetry.postValue(true)
                }
                is NetworkResponse.NetworkError -> {
                    _errorMessage.postValue(resourcesHelper.networkError)
                    _showLoading.postValue(false)
                    _showRetry.postValue(true)
                }
                is NetworkResponse.UnknownError -> {
                    _errorMessage.postValue(followersData.error.message.toString())
                    _showLoading.postValue(false)
                    _showRetry.postValue(true)
                }
            }
        }
    }

    private fun loadFollowing() {
        _showLoading.value = true
        _showRetry.value = false
        viewModelScope.launch(Dispatchers.IO) {
            when (val followingData = userRepository.getUserFollowing(usersLoginFromArgs)) {
                is NetworkResponse.Success -> {
                    _following.postValue(followingData.body)
                    _showLoading.postValue(false)
                }
                is NetworkResponse.ServerError -> {
                    _errorMessage.postValue(followingData.body?.message.toString())
                    _showLoading.postValue(false)
                }
                is NetworkResponse.NetworkError -> {
                    _errorMessage.postValue(resourcesHelper.networkError)
                    _showLoading.postValue(false)
                }
                is NetworkResponse.UnknownError -> {
                    _errorMessage.postValue(followingData.error.message.toString())
                    _showLoading.postValue(false)
                }
            }
        }
    }

    fun retry() {
        loadFollowers()
        loadFollowing()
    }

    init {
        loadFollowers()
        loadFollowing()
    }
}