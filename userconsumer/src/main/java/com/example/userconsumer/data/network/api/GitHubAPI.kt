package com.example.userconsumer.data.network.api

import com.example.userconsumer.BuildConfig
import com.example.userconsumer.data.model.User
import com.example.userconsumer.data.network.response.ErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GitHubAPI {


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