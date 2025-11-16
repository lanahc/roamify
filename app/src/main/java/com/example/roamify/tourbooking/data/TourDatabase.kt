package com.example.roamify.tourbooking.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Add Booking::class and increase version number to 3
@Database(entities = [Tour::class, User::class, Booking::class], version = 4, exportSchema = false)
abstract class TourDatabase : RoomDatabase() {

    abstract fun tourDao(): TourDao
    abstract fun userDao(): UserDao
    abstract fun bookingDao(): BookingDao // Add this abstract function

    companion object {
        @Volatile
        private var INSTANCE: TourDatabase? = null

        fun getDatabase(context: Context): TourDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TourDatabase::class.java,
                    "tour_database"
                )
                    .fallbackToDestructiveMigration() // Easiest for development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
