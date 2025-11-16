package com.example.roamify.tourbooking.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    @Update
    suspend fun updateBooking(booking: Booking)

    // Gets all bookings for the admin view
    @Query("SELECT * FROM bookings ORDER BY bookingId DESC")
    fun getAllBookings(): Flow<List<Booking>>
    // Gets a specific booking by its ID
    @Query("SELECT * FROM bookings WHERE bookingId = :bookingId")
    suspend fun getBookingById(bookingId: Long): Booking?

    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY bookingId DESC")
    fun getBookingsByUserId(userId: Int): Flow<List<Booking>>

}
