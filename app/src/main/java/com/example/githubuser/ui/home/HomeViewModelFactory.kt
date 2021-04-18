package com.example.githubuser.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.network.repository.UserRepository
import com.example.githubuser.helper.ResourcesHelper

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val userRepository: UserRepository,
    private val resourcesHelper: ResourcesHelper
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(userRepository, resourcesHelper) as T
    }

}