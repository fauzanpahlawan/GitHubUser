package com.example.userconsumer.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userconsumer.data.model.User
import com.example.userconsumer.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty

    fun loadUsers() {
        _isEmpty.value = false
        _isLoading.value = true
        users.value.let {
            viewModelScope.launch(Dispatchers.IO) {
                val nUsers = userRepository.fetchUsers()
                if (nUsers.isEmpty()) {
                    _isEmpty.postValue(true)
                }
                if (nUsers != it) {
                    _users.postValue(nUsers)
                }
                _isLoading.postValue(false)
            }
        }
    }


    init {
        loadUsers()
    }
}