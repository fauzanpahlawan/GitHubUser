package com.example.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import com.example.githubuser.data.network.repository.UserRepository

class FavoriteViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getAllUsers() = userRepository.getAllUsers()
}