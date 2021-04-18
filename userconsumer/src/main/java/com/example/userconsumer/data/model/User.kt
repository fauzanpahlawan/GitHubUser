package com.example.userconsumer.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class User(
    var id: Long = 0,
    var avatar_url: String = "Not Specified",
    var company: String = "Not Specified",
    var followers: Int = 0,
    var followers_url: String = "Not Specified",
    var following: Int = 0,
    var following_url: String = "Not Specified",
    var location: String = "Not Specified",
    var login: String = "Not Specified",
    var name: String = "Not Specified",
    var public_repos: Int = 0
) : Parcelable