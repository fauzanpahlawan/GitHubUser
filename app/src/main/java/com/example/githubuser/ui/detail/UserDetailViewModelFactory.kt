package com.example.githubuser.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.network.repository.UserRepository
import com.example.githubuser.helper.ResourcesHelper

@Suppress("UNCHECKED_CAST")
class UserDetailViewModelFactory(
    private val userRepository: UserRepository,
    private val resourcesHelper: ResourcesHelper,
    private val userAvatarUrlFromArgs: String,
    private val userLoginFromArgs: String,
    private val userIdFromArgs: Long
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserDetailViewModel(
            userRepository,
            resourcesHelper,
            userAvatarUrlFromArgs,
            userLoginFromArgs,
            userIdFromArgs
        ) as T
    }

}