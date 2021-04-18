package com.example.userconsumer.data.datasource

import android.content.ContentResolver
import android.net.Uri
import com.example.userconsumer.data.db.GithubUserDatabaseContract
import com.example.userconsumer.data.db.GithubUserDatabaseContract.CONTENT_URI
import com.example.userconsumer.data.db.MappingHelper
import com.example.userconsumer.data.model.User

class UserDataSource(private val contentResolver: ContentResolver) {
    fun fetchUsers(): List<User> {
        return MappingHelper.mapCursorToList(
            contentResolver.query(
                CONTENT_URI,
                null,
                null,
                null,
                null
            )
        )
    }

    fun deleteUsers(id: Long) {
        val uriWithId = Uri.Builder().scheme(GithubUserDatabaseContract.SCHEME).authority(
            GithubUserDatabaseContract.AUTHORITY
        )
            .appendPath(GithubUserDatabaseContract.TABLE_NAME)
            .appendPath("$id")
            .build()
        contentResolver.delete(uriWithId, null, null)
    }
}