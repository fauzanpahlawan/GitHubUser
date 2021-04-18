package com.example.githubuser.data.network.response

data class ErrorResponse(
    val message: String,
    val errors: List<Error>
)