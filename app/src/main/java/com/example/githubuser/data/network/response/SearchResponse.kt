package com.example.githubuser.data.network.response

import com.example.githubuser.data.model.User

data class SearchResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<User>
)