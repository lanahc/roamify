package com.example.roamify.tourbooking.data

// data/TourDao.kt
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TourDao {
    // Admin: Get all tours for management
    @Query("SELECT * FROM tours ORDER BY title ASC")
    fun getAllTours(): Flow<List<Tour>>

    // User: Get available tours (simple example)
    @Query("SELECT * FROM tours WHERE availableSlots > 0 ORDER BY price ASC")
    fun getAvailableTours(): Flow<List<Tour>>

    // Admin/User: CRUD Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tour: Tour)

    @Update
    suspend fun update(tour: Tour)

    @Delete
    suspend fun delete(tour: Tour)
}
