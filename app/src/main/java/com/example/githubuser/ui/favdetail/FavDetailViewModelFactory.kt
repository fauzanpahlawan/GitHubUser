package com.example.githubuser.ui.favdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.model.User
import com.example.githubuser.data.network.repository.UserRepository

@Suppress("UNCHECKED_CAST")
class FavDetailViewModelFactory(
    private val userRepository: UserRepository,
    private val user: User
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavDetailViewModel(userRepository, user) as T
    }
}