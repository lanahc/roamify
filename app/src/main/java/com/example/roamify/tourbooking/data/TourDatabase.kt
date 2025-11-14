package com.example.roamify.tourbooking.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Define all entities and database version
// Tour and TourDao are now in this package, so no need for explicit imports of Tour and TourDao
@Database(entities = [Tour::class], version = 1, exportSchema = false)
abstract class TourDatabase : RoomDatabase() {

    // Expose the DAO
    abstract fun tourDao(): TourDao

    companion object {
        @Volatile
        private var INSTANCE: TourDatabase? = null

        fun getDatabase(context: Context): TourDatabase {
            // If the INSTANCE is not null, then return it,
            // otherwise create a new database instance.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TourDatabase::class.java,
                    "tour_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // Return instance
                instance
            }
        }
    }
}