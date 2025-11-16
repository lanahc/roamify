package com.example.roamify

import android.app.Application
import com.example.roamify.tourbooking.data.TourDatabase
import com.example.roamify.tourbooking.data.TourRepository
import com.example.roamify.tourbooking.ui.TourViewModelFactory

class RoamifyApplication : Application() {
    // Lazily initialize the database
    private val database by lazy { TourDatabase.getDatabase(this) }

    // --- MODIFICATION: Pass both database.tourDao() and database.userDao() ---
    private val repository by lazy { TourRepository(database.tourDao(), database.userDao(), database.bookingDao()) }

    // ViewModelFactory now uses the correctly initialized repository
    val viewModelFactory by lazy { TourViewModelFactory(repository) }
}
