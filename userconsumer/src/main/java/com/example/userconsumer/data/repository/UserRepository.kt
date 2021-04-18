package com.example.userconsumer.data.repository

import com.example.userconsumer.data.datasource.UserDataSource
import com.example.userconsumer.data.model.User
import com.example.userconsumer.data.network.api.GitHubAPI
import com.example.userconsumer.data.network.response.ErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val gitHubAPI: GitHubAPI, private val userDataSource: UserDataSource) {
    suspend fun getUserFollowers(userLogin: String?): NetworkResponse<List<User>, ErrorResponse> =
        gitHubAPI.getUserFollowers(userLogin = userLogin)

    suspend fun getUserFollowing(userLogin: String?): NetworkResponse<List<User>, ErrorResponse> =
        gitHubAPI.getUsersFollowing(userLogin = userLogin)

    suspend fun fetchUsers(): List<User> = withContext(Dispatchers.IO) {
        userDataSource.fetchUsers()
    }

    suspend fun deleteUser(id: Long) {
        withContext(Dispatchers.IO) {
            userDataSource.deleteUsers(id)
        }
    }
}