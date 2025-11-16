package com.example.roamify.tourbooking.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.roamify.tourbooking.data.Booking
import com.example.roamify.tourbooking.data.BookingStatus
import com.example.roamify.tourbooking.data.TourRepository
import kotlinx.coroutines.launch

class BookingManagementViewModel(private val repository: TourRepository) : ViewModel() {

    // Observe all bookings from the repository and expose them as LiveData.
    val allBookings: LiveData<List<Booking>> = repository.allBookings.asLiveData()

    /**
     * Updates a booking's status to CONFIRMED.
     */
    fun confirmBooking(booking: Booking) {
        viewModelScope.launch {
            // Create a copy of the booking with the new status
            val updatedBooking = booking.copy(status = BookingStatus.CONFIRMED)
            repository.updateBooking(updatedBooking)
        }
    }

    /**
     * Updates a booking's status to CANCELLED.
     * Note: This does not yet refund tour slots. That's a more complex step for later.
     */
    fun cancelBooking(booking: Booking) {
        viewModelScope.launch {
            val updatedBooking = booking.copy(status = BookingStatus.CANCELLED)
            repository.updateBooking(updatedBooking)
        }
    }
}
