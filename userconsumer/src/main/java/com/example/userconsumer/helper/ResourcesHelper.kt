package com.example.userconsumer.helper

import android.content.Context
import com.example.userconsumer.R

class ResourcesHelper(private val applicationContext: Context) {
    val networkError
        get() = applicationContext.getString(R.string.network_error)
}