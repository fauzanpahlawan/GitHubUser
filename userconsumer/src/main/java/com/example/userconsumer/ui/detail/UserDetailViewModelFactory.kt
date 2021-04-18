package com.example.userconsumer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.userconsumer.data.model.User
import com.example.userconsumer.data.repository.UserRepository

@Suppress("UNCHECKED_CAST")
class UserDetailViewModelFactory(
    private val userRepository: UserRepository,
    private val user: User

) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserDetailViewModel(
            userRepository,
            user
        ) as T
    }

}