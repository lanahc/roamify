package com.example.roamify.tourbooking.data


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Custom exception used for clean error handling during booking.
 * PLACE THIS IN data/TourRepository.kt (or util/TourSoldOutException.kt if you prefer)
 */
class TourSoldOutException : Exception("The selected tour has no more available slots.")

/**
 * The Repository abstracts the data layer, providing a clean API to the ViewModels.
 * It handles logic, like the complex booking transaction, and manages thread safety.
 */
class TourRepository(private val tourDao: TourDao) {

    // Mutex for thread-safe access to booking logic, ensuring atomic updates
    private val bookingMutex = Mutex()

    // Expose data flows for real-time updates to ViewModels
    val allTours: Flow<List<Tour>> = tourDao.getAllTours()
    val availableTours: Flow<List<Tour>> = tourDao.getAvailableTours()

    suspend fun insertTour(tour: Tour) {
        tourDao.insert(tour)
    }

    suspend fun updateTour(tour: Tour) {
        tourDao.update(tour)
    }

    suspend fun deleteTour(tour: Tour) {
        tourDao.delete(tour)
    }

    /**
     * Handles the transactional logic for booking a tour:
     * 1. Fetches the tour object.
     * 2. Checks if slots are available.
     * 3. Decrements available slots.
     * 4. Updates the tour in the database.
     * @throws TourSoldOutException if no slots are available.
     */
    suspend fun bookTour(tourId: Long) = bookingMutex.withLock {
        val tour = tourDao.getTourById(tourId)

        if (tour == null) {
            throw IllegalArgumentException("Tour not found with ID: $tourId")
        }

        if (tour.availableSlots > 0) {
            // Create a copy of the tour with one fewer available slot
            val updatedTour = tour.copy(availableSlots = tour.availableSlots - 1)
            tourDao.update(updatedTour)
        } else {
            // Throw custom exception if sold out
            throw TourSoldOutException()
        }
    }
}
