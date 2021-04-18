package com.example.userconsumer.data.db

import android.net.Uri
import android.provider.BaseColumns

object GithubUserDatabaseContract {
    const val AUTHORITY = "com.example.githubuser"
    const val SCHEME = "content"

    const val TABLE_NAME = "GitHubUser"
    const val COLUMN_ID = BaseColumns._ID
    const val COLUMN_AVATAR_URL = "avatar_url"
    const val COLUMN_COMPANY = "company"
    const val COLUMN_FOLLOWERS = "followers"
    const val COLUMN_FOLLOWERS_URL = "followers_url"
    const val COLUMN_FOLLOWING = "following"
    const val COLUMN_FOLLOWING_URL = "following_url"
    const val COLUMN_LOCATION = "location"
    const val COLUMN_LOGIN = "login"
    const val COLUMN_NAME = "name"
    const val COLUMN_PUBLIC_REPOS = "public_repos"

    val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(TABLE_NAME)
        .build()
}