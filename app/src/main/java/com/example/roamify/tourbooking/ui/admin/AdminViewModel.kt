package com.example.roamify.tourbooking.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.roamify.tourbooking.data.Tour
import com.example.roamify.tourbooking.data.TourRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the Admin Page: Handles CRUD operations and exposes all tours.
 */
class AdminViewModel(private val repository: TourRepository) : ViewModel() {

    // Expose all tours as LiveData, derived from the Flow in the Repository
    val allTours: LiveData<List<Tour>> = repository.allTours.asLiveData()

    private val _adminStatus = MutableLiveData<String>()
    val adminStatus: LiveData<String> = _adminStatus

    /**
     * Adds a new tour to the database.
     * -- FIXED -- Simplified the function to remove confusing 'capacity' parameter.
     */
    fun addNewTour(name: String, description: String, location: String, price: Double, maxCapacity: Int) {
        viewModelScope.launch {
            try {
                // The tour's capacity is set, and available slots start at the maximum.
                val newTour = Tour(
                    name = name,
                    description = description,
                    location = location,
                    price = price,
                    maxCapacity = maxCapacity,      // Correctly use the maxCapacity parameter
                    availableSlots = maxCapacity  // Available slots should equal max capacity at creation
                )
                repository.insertTour(newTour)
                _adminStatus.postValue("Tour '$name' successfully added!")
            } catch (e: Exception) {
                _adminStatus.postValue("Failed to add tour: ${e.message}")
            }
        }
    }

    /**
     * Updates an existing tour in the database.
     */
    fun updateTour(tour: Tour) {
        viewModelScope.launch {
            try {
                // IMPORTANT: When updating, we use the tour object passed from the UI
                repository.updateTour(tour)
                _adminStatus.postValue("Tour '${tour.name}' updated.")
            } catch (e: Exception) {
                _adminStatus.postValue("Failed to update tour: ${e.message}")
            }
        }
    }

    /**
     * Deletes a tour from the database.
     */
    fun deleteTour(tour: Tour) {
        viewModelScope.launch {
            try {
                repository.deleteTour(tour)
                _adminStatus.postValue("Tour '${tour.name}' deleted.")
            } catch (e: Exception) {
                _adminStatus.postValue("Failed to delete tour: ${e.message}")
            }
        }
    }
}
