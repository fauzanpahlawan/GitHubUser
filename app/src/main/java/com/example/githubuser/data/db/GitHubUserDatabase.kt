package com.example.githubuser.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.data.model.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class GitHubUserDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: GitHubUserDatabase? = null

        fun getDatabase(context: Context): GitHubUserDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    GitHubUserDatabase::class.java,
                    "GitHubUserDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}