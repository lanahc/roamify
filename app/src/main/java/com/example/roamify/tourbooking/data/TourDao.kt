package com.example.roamify.tourbooking.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the Tour entity.
 * This interface defines the methods used to perform CRUD operations on the 'tours' table.
 */
@Dao
interface TourDao {
    // Admin: Get all tours for management, ordered alphabetically
    @Query("SELECT * FROM tours ORDER BY title ASC")
    fun getAllTours(): Flow<List<Tour>>

    // User: Get tours with available slots, ordered by price
    @Query("SELECT * FROM tours WHERE availableSlots > 0 ORDER BY price ASC")
    fun getAvailableTours(): Flow<List<Tour>>

    // Insert a new tour or replace if conflict (e.g., same primary key)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tour: Tour)

    // Update an existing tour
    @Update
    suspend fun update(tour: Tour)

    // Delete a tour
    @Delete
    suspend fun delete(tour: Tour)

    // Utility function to get a single tour for booking logic
    @Query("SELECT * FROM tours WHERE tourId = :id")
    suspend fun getTourById(id: Long): Tour?
}