package com.example.githubuser.ui.detail

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

class UserDetailViewModel(
    private val userRepository: UserRepository,
    private val resourcesHelper: ResourcesHelper,
    private val userAvatarUrlFromArgs: String,
    private val userLoginFromArgs: String,
    private val userIdFromArgs: Long,
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _userAvatarUrl = MutableLiveData<String>()
    val userAvatarUrl: LiveData<String>
        get() = _userAvatarUrl

    private val _userLogin = MutableLiveData<String>()
    val userLogin: LiveData<String>
        get() = _userLogin

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _showLoadingImg = MutableLiveData<Boolean>()
    val showLoadingImg: LiveData<Boolean>
        get() = _showLoadingImg

    private val _showRetry = MutableLiveData<Boolean>()
    val showRetry: LiveData<Boolean>
        get() = _showRetry

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _insertedUser = MutableLiveData<Boolean>()
    val insertedUser: LiveData<Boolean>
        get() = _insertedUser

    fun loadUser() {
        _userAvatarUrl.value = userAvatarUrlFromArgs
        _userLogin.value = userLoginFromArgs
        showFeedBackValue()
        viewModelScope.launch(Dispatchers.IO) {
            when (val userData = userRepository.getUser(userLogin = userLogin.value)) {
                is NetworkResponse.Success -> {
                    _user.postValue(userData.body)
                    showFeedBackPostValue(loadingImg = false, retryBtn = false)
                }
                is NetworkResponse.ServerError -> {
                    _errorMessage.postValue(userData.body?.message.toString())
                    showFeedBackPostValue(loadingImg = true, retryBtn = true)
                }
                is NetworkResponse.NetworkError -> {
                    _errorMessage.postValue(resourcesHelper.networkError)
                    showFeedBackPostValue(loadingImg = true, retryBtn = true)
                }
                is NetworkResponse.UnknownError -> {
                    _errorMessage.postValue(userData.error.message.toString())
                    showFeedBackPostValue(loadingImg = true, retryBtn = true)
                }
            }
        }
    }

    private fun showFeedBackValue() {
        _isLoading.postValue(true)
        _showLoadingImg.postValue(true)
        _showRetry.postValue(false)
    }

    private fun showFeedBackPostValue(loadingImg: Boolean, retryBtn: Boolean) {
        _isLoading.postValue(false)
        _showLoadingImg.postValue(loadingImg)
        _showRetry.postValue(retryBtn)
    }

    private val _fUser = MutableLiveData<User>()
    val fUser: LiveData<User>
        get() = _fUser

    fun setIsFavorite(boolean: Boolean) {
        _isFavorite.value = boolean
    }

    fun onFavClicked() {
        isFavorite.value?.let { isFav ->
            viewModelScope.launch(Dispatchers.IO) {
                if (isFav) {
                    userRepository.removeFromFavorite(user.value as User)
                    _isFavorite.postValue(false)
                    checkInsertedUser(userIdFromArgs)
                } else {
                    userRepository.addToFavorite(user.value as User)
                    _isFavorite.postValue(true)
                    checkInsertedUser(userIdFromArgs)
                }
            }
        }
    }

    private fun checkInsertedUser(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val user: User? = userRepository.getUserById(userId)
            if (user != null) {
                _insertedUser.postValue(true)
            } else {
                _insertedUser.postValue(false)
            }
        }
    }

    fun isUserFav() {
        viewModelScope.launch(Dispatchers.IO) {
            _fUser.postValue(userRepository.getUserById(userIdFromArgs))
        }
    }

    init {
        loadUser()
        isUserFav()
    }
}