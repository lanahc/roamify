package com.example.roamify.tourbooking.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TourDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tour: Tour)

    @Update
    suspend fun update(tour: Tour)

    @Delete
    suspend fun delete(tour: Tour)

    @Query("SELECT * from tours ORDER BY name ASC")
    fun getAllTours(): Flow<List<Tour>>

    // Get tours that have at least one slot available
    @Query("SELECT * from tours WHERE availableSlots > 0 ORDER BY name ASC")
    fun getAvailableTours(): Flow<List<Tour>>

    // Get a specific tour by its ID
    @Query("SELECT * from tours WHERE tourId = :tourId")
    fun getTourById(tourId: Long): Flow<Tour?> // Changed to Long

    @Query("SELECT * FROM tours WHERE tourId = :tourId")
    suspend fun getTourByIdNonFlow(tourId: Long): Tour?

}
