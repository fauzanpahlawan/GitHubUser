package com.example.userconsumer.data.network.response

data class ErrorResponse(
    val message: String,
    val errors: List<Error>
)