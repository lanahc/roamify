package com.example.roamify.tourbooking.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.roamify.tourbooking.data.AppDatabase
import com.example.roamify.tourbooking.data.TourRepository
import com.example.roamify.tourbooking.ui.admin.AdminViewModel
import com.example.roamify.tourbooking.ui.auth.AuthViewModel
import com.example.roamify.tourbooking.ui.booking.BookingViewModel
import com.example.roamify.tourbooking.ui.user.UserViewModel
import com.example.roamify.tourbooking.ui.admin.BookingManagementViewModel
/**
 * --- UPDATED ---
 * Factory for creating all ViewModels. It can now accept an optional tourId
 * for creating the BookingViewModel.
 */
class TourViewModelFactory(
    private val context: Context,
    private val tourId: Long? = null // Optional parameter for BookingViewModel
) : ViewModelProvider.Factory {

    // Lazily initialized repository to ensure it's created only once.
    private val repository: TourRepository by lazy {
        val database = AppDatabase.getDatabase(context.applicationContext)
        TourRepository(
            tourDao = database.tourDao(),
            userDao = database.userDao(),
            bookingDao = database.bookingDao()
        )
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            // Special case for BookingViewModel
            modelClass.isAssignableFrom(BookingViewModel::class.java) -> {
                // Ensure tourId is provided, otherwise crash. This is a developer error.
                requireNotNull(tourId) { "Tour ID must be provided to create BookingViewModel" }
                BookingViewModel(tourId, repository) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AdminViewModel::class.java) -> {
                AdminViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(repository) as T
            }
            modelClass.isAssignableFrom(BookingManagementViewModel::class.java) -> {
                BookingManagementViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
