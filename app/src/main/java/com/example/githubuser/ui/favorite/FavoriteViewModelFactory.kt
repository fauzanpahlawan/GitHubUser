package com.example.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.network.repository.UserRepository

@Suppress("UNCHECKED_CAST")
class FavoriteViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoriteViewModel(userRepository) as T
    }
}