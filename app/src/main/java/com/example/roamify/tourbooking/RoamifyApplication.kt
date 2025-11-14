package com.example.roamify

import android.app.Application
// UPDATED: Imports now include 'tourbooking'
import com.example.roamify.tourbooking.data.TourDatabase
import com.example.roamify.tourbooking.data.TourRepository
import com.example.roamify.tourbooking.ui.TourViewModelFactory

/**
 * Custom Application class used for initializing and holding application-wide dependencies.
 * This ensures the database and repository are created only once and live for the entire app lifecycle.
 */
class RoamifyApplication : Application() {

    // Use lazy delegation to ensure the database is initialized only when accessed for the first time.
    // The context 'this' refers to the Application context.
    private val database by lazy {
        TourDatabase.getDatabase(this)
    }

    // Use lazy delegation to ensure the repository is initialized only when accessed for the first time.
    val repository by lazy {
        TourRepository(database.tourDao())
    }

    // The factory instance, also lazily initialized.
    val viewModelFactory by lazy {
        TourViewModelFactory(repository)
    }
}