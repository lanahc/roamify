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
     */
    fun addNewTour(title: String, description: String, capacity: Int, price: Double) {
        viewModelScope.launch {
            try {
                val newTour = Tour(
                    title = title,
                    description = description,
                    maxCapacity = capacity,
                    availableSlots = capacity, // Start with all slots available
                    price = price
                )
                repository.insertTour(newTour)
                _adminStatus.postValue("Tour '$title' successfully added!")
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
                // IMPORTANT: When updating, we use the tour object passed from the UI (which already has the updated fields)
                repository.updateTour(tour)
                _adminStatus.postValue("Tour '${tour.title}' updated.")
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
                _adminStatus.postValue("Tour '${tour.title}' deleted.")
            } catch (e: Exception) {
                _adminStatus.postValue("Failed to delete tour: ${e.message}")
            }
        }
    }
}