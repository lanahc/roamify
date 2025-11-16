package com.example.roamify.tourbooking.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// Defines the possible statuses for a booking
enum class BookingStatus {
    PENDING, CONFIRMED, CANCELLED
}

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true)
    val bookingId: Long = 0,
    val tourId: Long, // Foreign key to the Tour
    val userId: Int, // Foreign key to the User (we'll add this later)

    val bookingDate: String, // The date the user wants to go on the tour
    val numberOfPeople: Int,
    val requiresCarRental: Boolean,
    val stayDurationInDays: Int,

    // Default status is PENDING when a booking is created
    val status: BookingStatus = BookingStatus.PENDING
)
