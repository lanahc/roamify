package com.example.roamify.tourbooking.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.roamify.tourbooking.data.Tour
import com.example.roamify.tourbooking.data.TourRepository
import com.example.roamify.tourbooking.data.TourSoldOutException // Exception is in data package
import kotlinx.coroutines.launch

class UserViewModel(private val repository: TourRepository) : ViewModel() {

    // Expose only available tours to the user UI
    val availableTours: LiveData<List<Tour>> = repository.availableTours.asLiveData()

    private val _bookingStatus = MutableLiveData<String>()
    val bookingStatus: LiveData<String> = _bookingStatus

    /**
     * Attempts to book one slot for the given tour ID.
     */
    fun attemptBooking(tourId: Long) {
        viewModelScope.launch {
            try {
                // The Repository handles the safety and logic of the transaction.
                repository.bookTour(tourId)
                _bookingStatus.postValue("Booking successful! Enjoy your trip.")
            } catch (e: TourSoldOutException) {
                _bookingStatus.postValue("Failed: This tour is now sold out.")
            } catch (e: Exception) {
                _bookingStatus.postValue("Booking failed due to an error.")
            }
        }
    }
}
