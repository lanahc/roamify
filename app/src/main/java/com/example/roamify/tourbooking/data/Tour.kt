package com.example.roamify.tourbooking.data

// data/Tour.kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tours")
data class Tour(
    @PrimaryKey(autoGenerate = true)
    val tourId: Long = 0,
    val title: String,
    val description: String,
    val price: Double,
    val maxCapacity: Int,
    val availableSlots: Int,
    val imageUrl: String? = null,
    val isFeatured: Boolean = false
)
