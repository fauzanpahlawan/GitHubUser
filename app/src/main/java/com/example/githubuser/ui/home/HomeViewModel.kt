package com.example.githubuser.ui.home

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

class HomeViewModel(
    private val userRepository: UserRepository,
    private val resourcesHelper: ResourcesHelper
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val _showRetry = MutableLiveData<Boolean>()
    val showRetry: LiveData<Boolean>
        get() = _showRetry

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _searching = MutableLiveData<Boolean>()
    private val searching: LiveData<Boolean>
        get() = _searching

    private val _searchUser = MutableLiveData<String>()
    private val searchUser: LiveData<String>
        get() = _searchUser

    private fun loadGitHubUsers() {
        _searching.value = false
        _users.value = listOf()
        showFeedBackValue()
        _isEmpty.value = false
        viewModelScope.launch(Dispatchers.IO) {
            when (val usersData = userRepository.getUsers()) {
                is NetworkResponse.Success -> {
                    _users.postValue(usersData.body)
                    showFeedBackPostValue(false)
                }
                is NetworkResponse.ServerError -> {
                    _errorMessage.postValue(usersData.body?.message.toString())
                    showFeedBackPostValue(true)
                }
                is NetworkResponse.NetworkError -> {
                    _errorMessage.postValue(resourcesHelper.networkError)
                    showFeedBackPostValue(true)
                }
                is NetworkResponse.UnknownError -> {
                    _errorMessage.postValue(usersData.error.message.toString())
                    showFeedBackPostValue(true)
                }
            }
        }
    }

    fun getSearchGitHubUsers(userLogin: String?) {
        _searching.value = true
        _searchUser.value = userLogin
        _isEmpty.value = false
        showFeedBackValue()
        viewModelScope.launch(Dispatchers.IO) {
            when (val usersData = userRepository.getSearchUser(userLogin)) {
                is NetworkResponse.Success -> {
                    _users.postValue(usersData.body.items)
                    isDataEmpty(usersData.body.items)
                    showFeedBackPostValue(false)
                }
                is NetworkResponse.ServerError -> {
                    _users.postValue(listOf())
                    _errorMessage.postValue(usersData.body?.message.toString())
                    showFeedBackPostValue(true)
                }
                is NetworkResponse.NetworkError -> {
                    _users.postValue(listOf())
                    _errorMessage.postValue(resourcesHelper.networkError)
                    showFeedBackPostValue(true)
                }
                is NetworkResponse.UnknownError -> {
                    _users.postValue(listOf())
                    _errorMessage.postValue(usersData.error.message.toString())
                    showFeedBackPostValue(true)
                }
            }
        }
    }

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty

    private fun isDataEmpty(user: List<User>) {
        if (user.isEmpty()) {
            _isEmpty.postValue(true)
        }
    }

    fun retry() {
        if (searching.value == false) {
            loadGitHubUsers()
        } else {
            getSearchGitHubUsers(searchUser.value)
        }
    }

    private fun showFeedBackValue() {
        _showLoading.postValue(true)
        _showRetry.postValue(false)
    }

    private fun showFeedBackPostValue(retryBtn: Boolean) {
        _showLoading.postValue(false)
        _showRetry.postValue(retryBtn)
    }

    init {
        loadGitHubUsers()
    }
}