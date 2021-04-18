package com.example.userconsumer.data.network.response

data class Error(
    val code: String,
    val field: String,
    val resource: String
)