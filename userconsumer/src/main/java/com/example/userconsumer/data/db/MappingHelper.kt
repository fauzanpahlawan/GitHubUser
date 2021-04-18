package com.example.userconsumer.data.db

import android.database.Cursor
import com.example.userconsumer.data.db.GithubUserDatabaseContract.COLUMN_AVATAR_URL
import com.example.userconsumer.data.db.GithubUserDatabaseContract.COLUMN_COMPANY
import com.example.userconsumer.data.db.GithubUserDatabaseContract.COLUMN_FOLLOWERS
import com.example.userconsumer.data.db.GithubUserDatabaseContract.COLUMN_FOLLOWERS_URL
import com.example.userconsumer.data.db.GithubUserDatabaseContract.COLUMN_FOLLOWING
import com.example.userconsumer.data.db.GithubUserDatabaseContract.COLUMN_FOLLOWING_URL
import com.example.userconsumer.data.db.GithubUserDatabaseContract.COLUMN_ID
import com.example.userconsumer.data.db.GithubUserDatabaseContract.COLUMN_LOCATION
import com.example.userconsumer.data.db.GithubUserDatabaseContract.COLUMN_LOGIN
import com.example.userconsumer.data.db.GithubUserDatabaseContract.COLUMN_NAME
import com.example.userconsumer.data.db.GithubUserDatabaseContract.COLUMN_PUBLIC_REPOS
import com.example.userconsumer.data.model.User

object MappingHelper {
    fun mapCursorToList(userCursor: Cursor?): List<User> {
        val userList = mutableListOf<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val avatarUrl = getString(getColumnIndexOrThrow(COLUMN_AVATAR_URL))
                val company = getString(getColumnIndexOrThrow(COLUMN_COMPANY))
                val followers = getInt(getColumnIndexOrThrow(COLUMN_FOLLOWERS))
                val followersUrl = getString(getColumnIndexOrThrow(COLUMN_FOLLOWERS_URL))
                val following = getInt(getColumnIndexOrThrow(COLUMN_FOLLOWING))
                val followingUrl = getString(getColumnIndexOrThrow(COLUMN_FOLLOWING_URL))
                val location = getString(getColumnIndexOrThrow(COLUMN_LOCATION))
                val login = getString(getColumnIndexOrThrow(COLUMN_LOGIN))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val publicRepos = getInt(getColumnIndexOrThrow(COLUMN_PUBLIC_REPOS))
                val user = User(
                    id,
                    avatarUrl,
                    company,
                    followers,
                    followersUrl,
                    following,
                    followingUrl,
                    location,
                    login,
                    name,
                    publicRepos
                )
                userList.add(user)
            }
            close()
        }
        return userList
    }

}