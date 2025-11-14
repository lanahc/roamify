package com.example.roamify.tourbooking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.roamify.tourbooking.data.TourRepository
import com.example.roamify.tourbooking.ui.admin.AdminViewModel // <-- NEW: Import AdminViewModel from its new package
import com.example.roamify.tourbooking.ui.user.UserViewModel   // <-- NEW: Import UserViewModel from its new package

/**
 * Factory class to instantiate ViewModels with the required TourRepository dependency.
 * This is necessary because ViewModels with custom constructors cannot be created by the system directly.
 */
class TourViewModelFactory(private val repository: TourRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel is the AdminViewModel
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            return AdminViewModel(repository) as T
        }
        // Check if the requested ViewModel is the UserViewModel
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository) as T
        }
        // Throw an error if the factory is asked to create an unsupported ViewModel
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}