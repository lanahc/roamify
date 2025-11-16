package com.example.roamify.tourbooking.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object (DAO) for the User entity.
 * This interface provides the methods that the rest of the app uses
 * to interact with the 'users' table in the database.
 */
@Dao
interface UserDao {
    /**
     * Inserts a new user into the database.
     * If a user with the same primary key already exists, it will be ignored.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    /**
     * Finds a user by their name and password.
     * This is used for the login functionality.
     * @return A [User] object if a match is found, otherwise null.
     */
    @Query("SELECT * FROM users WHERE name = :name AND password = :password LIMIT 1")
    suspend fun loginUser(name: String, password: String): User?
}
