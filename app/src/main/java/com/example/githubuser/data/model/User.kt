package com.example.githubuser.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.githubuser.data.db.GithubUserDatabaseContract.COLUMN_AVATAR_URL
import com.example.githubuser.data.db.GithubUserDatabaseContract.COLUMN_COMPANY
import com.example.githubuser.data.db.GithubUserDatabaseContract.COLUMN_FOLLOWERS
import com.example.githubuser.data.db.GithubUserDatabaseContract.COLUMN_FOLLOWERS_URL
import com.example.githubuser.data.db.GithubUserDatabaseContract.COLUMN_FOLLOWING
import com.example.githubuser.data.db.GithubUserDatabaseContract.COLUMN_FOLLOWING_URL
import com.example.githubuser.data.db.GithubUserDatabaseContract.COLUMN_ID
import com.example.githubuser.data.db.GithubUserDatabaseContract.COLUMN_LOCATION
import com.example.githubuser.data.db.GithubUserDatabaseContract.COLUMN_LOGIN
import com.example.githubuser.data.db.GithubUserDatabaseContract.COLUMN_NAME
import com.example.githubuser.data.db.GithubUserDatabaseContract.COLUMN_PUBLIC_REPOS
import com.example.githubuser.data.db.GithubUserDatabaseContract.TABLE_NAME
import kotlinx.android.parcel.Parcelize

@Parcelize

@Entity(tableName = TABLE_NAME)
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var id: Long = 0,
    @ColumnInfo(name = COLUMN_AVATAR_URL, defaultValue = "Not Specified")
    var avatar_url: String = "Not Specified",
    @ColumnInfo(name = COLUMN_COMPANY, defaultValue = "Not Specified")
    var company: String = "Not Specified",
    @ColumnInfo(name = COLUMN_FOLLOWERS)
    var followers: Int = 0,
    @ColumnInfo(name = COLUMN_FOLLOWERS_URL, defaultValue = "Not Specified")
    var followers_url: String = "Not Specified",
    @ColumnInfo(name = COLUMN_FOLLOWING)
    var following: Int = 0,
    @ColumnInfo(name = COLUMN_FOLLOWING_URL, defaultValue = "Not Specified")
    var following_url: String = "Not Specified",
    @ColumnInfo(name = COLUMN_LOCATION, defaultValue = "Not Specified")
    var location: String = "Not Specified",
    @ColumnInfo(name = COLUMN_LOGIN, defaultValue = "Not Specified")
    var login: String = "Not Specified",
    @ColumnInfo(name = COLUMN_NAME, defaultValue = "Not Specified")
    var name: String = "Not Specified",
    @ColumnInfo(name = COLUMN_PUBLIC_REPOS)
    var public_repos: Int = 0
) : Parcelable