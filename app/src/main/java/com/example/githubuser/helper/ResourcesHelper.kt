package com.example.githubuser.helper

import android.content.Context
import com.example.githubuser.R

class ResourcesHelper(private val applicationContext: Context) {
    val networkError
        get() = applicationContext.getString(R.string.network_error)
}