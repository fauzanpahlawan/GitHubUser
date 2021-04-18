package com.example.userconsumer.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userconsumer.data.model.User
import com.example.userconsumer.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val userRepository: UserRepository,
    private val mUser: User
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    private fun setUser() {
        _user.value = mUser
        _isFavorite.value = true
    }

    fun onFavClicked() {
        removeFromFavorite(mUser)
        _isFavorite.postValue(false)
    }

    fun removeFromFavorite(user: User) {
        viewModelScope.launch(Dispatchers.Main) {
            userRepository.deleteUser(user.id)
        }
    }

    init {
        setUser()
    }
}