package com.example.roamify.tourbooking.ui.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.roamify.tourbooking.data.Tour
import com.example.roamify.tourbooking.data.TourRepository
import com.example.roamify.tourbooking.data.TourSoldOutException
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Enum to represent the state of the booking process
enum class BookingUiState {
    LOADING, SUCCESS, SOLD_OUT, ERROR
}

class BookingViewModel(
    private val tourId: Long,
    private val repository: TourRepository
) : ViewModel() {

    // Holds the details of the tour being booked.
    // It's a LiveData that observes the database for any changes to this specific tour.
    val tour: LiveData<Tour?> = repository.getTourById(tourId).asLiveData()

    // Holds the current state of the UI (e.g., loading, success, error)
    private val _uiState = MutableLiveData<BookingUiState>()
    val uiState: LiveData<BookingUiState> = _uiState

    /**
     * The core function to execute the booking.
     * It's called when the user clicks the "Confirm Booking" button.
     */
    fun confirmBooking(
        userId: Int,            // We'll need to get this from a session manager later
        peopleCount: Int,
        wantsCar: Boolean,
        stayDuration: Int
    ) {
        viewModelScope.launch {
            _uiState.value = BookingUiState.LOADING
            try {
                // Get the current date as a string
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                // Call the repository to perform the booking transaction
                repository.createBookingAndUpdateTour(
                    currentUserId = userId, // This is hardcoded for now
                    currentTourId = tourId,
                    peopleCount = peopleCount,
                    rentalCar = wantsCar,
                    stayDays = stayDuration,
                    date = currentDate
                )
                _uiState.postValue(BookingUiState.SUCCESS)
            } catch (e: TourSoldOutException) {
                // Handle the specific "sold out" error
                _uiState.postValue(BookingUiState.SOLD_OUT)
            } catch (e: Exception) {
                // Handle all other potential errors
                _uiState.postValue(BookingUiState.ERROR)
            }
        }
    }
}
