package com.example.githubuser.ui.favdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.model.User
import com.example.githubuser.data.network.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavDetailViewModel(
    private val userRepository: UserRepository,
    private val userFromArgs: User
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    private val _insertedUser = MutableLiveData<Boolean>()
    val insertedUser: LiveData<Boolean>
        get() = _insertedUser

    private fun getUserById() {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(userRepository.getUserById(userFromArgs.id))
        }
    }

    fun setIsFavorite(boolean: Boolean) {
        _isFavorite.value = boolean
    }

    fun onFavClicked() {
        isFavorite.value?.let { isFav ->
            viewModelScope.launch(Dispatchers.IO) {
                if (isFav) {
                    userRepository.removeFromFavorite(user.value as User)
                    _isFavorite.postValue(false)
                    checkInsertedUser(userFromArgs.id)
                } else {
                    userRepository.addToFavorite(user.value as User)
                    _isFavorite.postValue(true)
                    checkInsertedUser(userFromArgs.id)
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

    private val _fUser = MutableLiveData<User>()
    val fUser: LiveData<User>
        get() = _fUser

    fun isUserFav() {
        viewModelScope.launch(Dispatchers.IO) {
            _fUser.postValue(userRepository.getUserById(userFromArgs.id))
        }
    }

    init {
        getUserById()
        isUserFav()
    }
}