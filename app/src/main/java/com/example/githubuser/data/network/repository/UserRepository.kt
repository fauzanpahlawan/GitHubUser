package com.example.githubuser.data.network.repository

import androidx.lifecycle.LiveData
import com.example.githubuser.data.db.GitHubUserDatabase
import com.example.githubuser.data.model.User
import com.example.githubuser.data.network.api.GitHubAPI
import com.example.githubuser.data.network.response.ErrorResponse
import com.example.githubuser.data.network.response.SearchResponse
import com.haroldadmin.cnradapter.NetworkResponse

class UserRepository(private val gitHubAPI: GitHubAPI, private val db: GitHubUserDatabase) {

    suspend fun getUsers(): NetworkResponse<List<User>, ErrorResponse> =
        gitHubAPI.getGitHubUsers()

    suspend fun getUser(userLogin: String?): NetworkResponse<User, ErrorResponse> =
        gitHubAPI.getGitHubUser(userLogin = userLogin)

    suspend fun getSearchUser(userLogin: String?): NetworkResponse<SearchResponse, ErrorResponse> =
        gitHubAPI.getSearchGitHubUsers(userLogin = userLogin)

    suspend fun getUserFollowers(userLogin: String?): NetworkResponse<List<User>, ErrorResponse> =
        gitHubAPI.getUserFollowers(userLogin = userLogin)

    suspend fun getUserFollowing(userLogin: String?): NetworkResponse<List<User>, ErrorResponse> =
        gitHubAPI.getUsersFollowing(userLogin = userLogin)

    suspend fun addToFavorite(user: User) {
        db.getUserDao().insertUser(user)
    }

    suspend fun removeFromFavorite(user: User) {
        db.getUserDao().deleteUser(user)
    }

    fun getAllUsers(): LiveData<List<User>> = db.getUserDao().getAllUser()

    suspend fun getUserById(id: Long): User = db.getUserDao().getUser(id)
}