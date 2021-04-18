package com.example.githubuser.data.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubuser.data.db.GitHubUserDatabase
import com.example.githubuser.data.db.GithubUserDatabaseContract.AUTHORITY
import com.example.githubuser.data.db.GithubUserDatabaseContract.CONTENT_URI
import com.example.githubuser.data.db.GithubUserDatabaseContract.TABLE_NAME

class UserProvider : ContentProvider() {

    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }

    init {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
        sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor: Cursor? = when (sUriMatcher.match(uri)) {
            USER -> GitHubUserDatabase.getDatabase(context!!).getUserDao().selectAll()
            USER_ID -> GitHubUserDatabase.getDatabase(context!!).getUserDao().selectUser(ContentUris.parseId(uri))
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
        cursor?.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val delete = when (sUriMatcher.match(uri)) {
            USER -> {
                throw IllegalArgumentException("Invalid URI, cannot delete without ID: $uri")
            }
            USER_ID -> {
                GitHubUserDatabase.getDatabase(context!!).getUserDao().deleteByUser(ContentUris.parseId(uri))
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return delete
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}