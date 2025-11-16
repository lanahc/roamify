package com.example.roamify.tourbooking.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * The main database class for the application.
 *
 * This abstract class extends RoomDatabase and serves as the main access point
 * to the persisted data.
 *
 * It lists all the entities (tables) that the database will contain and the DAOs
 * that provide methods for interacting with those tables.
 */
@Database(
    entities = [
        Tour::class,
        User::class,
        Booking::class
    ],
    version = 1, // Start with version 1. Increment this if you change the schema.
    exportSchema = false // Not needed for this project
)
@TypeConverters(Converters::class) // Register the TypeConverter for Date and Enum types
abstract class AppDatabase : RoomDatabase() {

    // Abstract functions that will return an instance of each DAO
    abstract fun tourDao(): TourDao
    abstract fun userDao(): UserDao
    abstract fun bookingDao(): BookingDao

    // Companion object allows us to create a single instance of the database (Singleton pattern)
    companion object {
        // The '@Volatile' annotation ensures that the INSTANCE variable is always up-to-date
        // and visible to all threads.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns the singleton instance of the database.
         * If it doesn't exist, it creates it in a thread-safe way.
         */
        fun getDatabase(context: Context): AppDatabase {
            // Return the existing instance if it's not null
            return INSTANCE ?: synchronized(this) {
                // If the instance is still null, build the database
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "roamify_database" // The name of the database file on the device
                ).build()
                INSTANCE = instance
                // Return the newly created instance
                instance
            }
        }
    }
}
