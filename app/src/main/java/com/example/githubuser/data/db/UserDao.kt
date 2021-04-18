package com.example.githubuser.data.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.data.db.GithubUserDatabaseContract.COLUMN_ID
import com.example.githubuser.data.db.GithubUserDatabaseContract.TABLE_NAME
import com.example.githubuser.data.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert
    suspend fun insertUsers(user: List<User>)

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllUser(): LiveData<List<User>>

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllUsersWidget(): List<User>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = :id")
    suspend fun getUser(id: Long): User

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    //content provider
    @Query("SELECT * FROM $TABLE_NAME")
    fun selectAll(): Cursor

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = :id")
    fun selectUser(id: Long): Cursor

    @Query("DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = :id")
    fun deleteByUser(id: Long): Int
}