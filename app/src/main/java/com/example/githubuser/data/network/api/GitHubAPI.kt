package com.example.githubuser.data.network.api

import com.example.githubuser.BuildConfig
import com.example.githubuser.data.model.User
import com.example.githubuser.data.network.response.ErrorResponse
import com.example.githubuser.data.network.response.SearchResponse
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubAPI {

    @GET("users")
    suspend fun getGitHubUsers(
        @Header(AUTHORIZATION) token: String = TOKEN
    ): NetworkResponse<List<User>, ErrorResponse>

    @GET("search/users")
    suspend fun getSearchGitHubUsers(
        @Header(AUTHORIZATION) token: String = TOKEN,
        @Query(QUERY) userLogin: String?
    ): NetworkResponse<SearchResponse, ErrorResponse>

    @GET("users/{userLogin}")
    suspend fun getGitHubUser(
        @Header(AUTHORIZATION) token: String = TOKEN,
        @Path("userLogin") userLogin: String?
    ): NetworkResponse<User, ErrorResponse>

    @GET("users/{userLogin}/followers")
    suspend fun getUserFollowers(
        @Header(AUTHORIZATION) token: String = TOKEN,
        @Path("userLogin") userLogin: String?
    ): NetworkResponse<List<User>, ErrorResponse>

    @GET("users/{userLogin}/following")
    suspend fun getUsersFollowing(
        @Header(AUTHORIZATION) token: String = TOKEN,
        @Path("userLogin") userLogin: String?
    ): NetworkResponse<List<User>, ErrorResponse>

    companion object {
        private const val BASE_URL = "https://api.github.com/"
        private const val TOKEN = BuildConfig.TOKEN
        private const val AUTHORIZATION = "authorization"
        private const val QUERY = "q"

        operator fun invoke(): GitHubAPI {
            return Retrofit.Builder()
                .addCallAdapterFactory(NetworkResponseAdapterFactory())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GitHubAPI::class.java)
        }
    }
}