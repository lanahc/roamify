package com.example.roamify.tourbooking.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.roamify.tourbooking.data.Tour
import com.example.roamify.tourbooking.data.TourRepository

class UserViewModel(repository: TourRepository) : ViewModel() {

    // --- Data Logic (This part is correct) ---
    // This converts the 'availableTours' Flow from the repository into LiveData.
    // The UI will observe this and automatically update whenever the database changes.
    val availableTours: LiveData<List<Tour>> = repository.availableTours.asLiveData()


    // --- Navigation Logic (This part was missing) ---
    // LiveData to hold the ID of the tour we want to navigate to.
    // It's a "Single Live Event" - it should only be observed once.
    private val _navigateToBookingForm = MutableLiveData<Long?>()
    val navigateToBookingForm: LiveData<Long?> = _navigateToBookingForm

    /**
     * Called by the UI when the "Book Now" button is clicked.
     * Sets the value of the navigation event LiveData.
     */
    fun onBookTourClicked(tourId: Long) {
        _navigateToBookingForm.value = tourId
    }

    /**
     * Called by the UI after navigation has been performed.
     * Resets the LiveData to null to prevent re-navigation on configuration change.
     */
    fun onNavigationComplete() {
        _navigateToBookingForm.value = null
    }
}
