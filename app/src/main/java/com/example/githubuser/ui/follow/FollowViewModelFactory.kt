package com.example.githubuser.ui.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.network.repository.UserRepository
import com.example.githubuser.helper.ResourcesHelper

@Suppress("UNCHECKED_CAST")
class FollowViewModelFactory(
    private val userRepository: UserRepository,
    private val userLoginFromArgs: String,
    private val resourcesHelper: ResourcesHelper
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FollowViewModel(userRepository, userLoginFromArgs, resourcesHelper) as T
    }

}