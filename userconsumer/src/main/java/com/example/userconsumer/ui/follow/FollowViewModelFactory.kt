package com.example.userconsumer.ui.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.userconsumer.data.repository.UserRepository
import com.example.userconsumer.helper.ResourcesHelper

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