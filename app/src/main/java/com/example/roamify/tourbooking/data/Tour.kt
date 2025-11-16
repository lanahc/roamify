package com.example.roamify.tourbooking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Defines the structure of a tour in the Room database.
 * @Entity(tableName = "tours"): The table name for this data class.
 */
@Entity(tableName = "tours")
data class Tour(
    // Primary key for the database, auto-generated
    @PrimaryKey(autoGenerate = true)
    val tourId: Long = 0,
    val name: String,
    val location: String,
    val description: String,
    val price: Double,
    val maxCapacity: Int,
    val availableSlots: Int,

)