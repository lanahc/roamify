package com.example.roamify.tourbooking.data

import androidx.compose.animation.core.copy
import androidx.compose.foundation.text2.input.insert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Custom exception used for clean error handling during booking.
 */
class TourSoldOutException : Exception("The selected tour has no more available slots.")

/**
 * The Repository abstracts the data layer, providing a clean API to the ViewModels.
 * It handles logic, like the complex booking transaction, and manages thread safety.
 *
 * --- UPDATED ---
 * It now manages Tour, User, and Booking data sources.
 */
// MODIFICATION 1: Update the constructor to accept all three DAOs.
class TourRepository(
    private val tourDao: TourDao,
    private val userDao: UserDao,
    private val bookingDao: BookingDao // <-- ADDED
) {

    // Mutex for thread-safe access to booking logic, ensuring atomic updates.
    private val bookingMutex = Mutex()

    // --- Tour-related data and functions ---
    val allTours: Flow<List<Tour>> = tourDao.getAllTours()
    val availableTours: Flow<List<Tour>> = tourDao.getAvailableTours()

    val allBookings: Flow<List<Booking>> = bookingDao.getAllBookings()

    suspend fun insertTour(tour: Tour) {
        tourDao.insert(tour)
    }

    suspend fun updateTour(tour: Tour) {
        tourDao.update(tour)
    }

    suspend fun deleteTour(tour: Tour) {
        tourDao.delete(tour)
    }
    suspend fun updateBooking(booking: Booking) {
        bookingDao.updateBooking(booking)
    }

    // MODIFICATION 2: ADD a function to get a single tour by ID.
    /**
     * Retrieves a single tour from the database by its ID.
     * This is crucial for the BookingViewModel to get tour details.
     */
    fun getTourById(tourId: Long): Flow<Tour?> {
        return tourDao.getTourById(tourId)
    }

    /**
     * Handles the transactional logic for creating a booking and updating tour slots.
     * This is an atomic operation to prevent race conditions.
     */

// ... inside TourRepository.kt ...

    /**
     * Handles the transactional logic for creating a booking and updating tour slots.
     * This version is updated to match the project's specific Booking.kt structure.
     */
    suspend fun createBookingAndUpdateTour(
        currentUserId: Int,
        currentTourId: Long,
        peopleCount: Int,
        rentalCar: Boolean,
        stayDays: Int,
        date: String
    ) = bookingMutex.withLock {
        // Use a non-flow version for the transaction
        val tour = tourDao.getTourByIdNonFlow(currentTourId)
            ?: throw IllegalArgumentException("Tour not found with ID: $currentTourId")

        if (tour.availableSlots >= peopleCount) {
            // 1. Create and insert the new booking record using YOUR Booking class structure
            val newBooking = Booking(
                userId = currentUserId,
                tourId = currentTourId,
                numberOfPeople = peopleCount,
                requiresCarRental = rentalCar,
                stayDurationInDays = stayDays,
                bookingDate = date
                // The status will default to PENDING as defined in your Booking.kt
            )
            // Use the function name from your BookingDao: insertBooking
            bookingDao.insertBooking(newBooking)

            // 2. Update the tour with fewer available slots
            val updatedTour = tour.copy(availableSlots = tour.availableSlots - peopleCount)
            tourDao.update(updatedTour)
        } else {
            // 3. Throw custom exception if not enough slots are available
            throw TourSoldOutException()
        }
    }

// ... rest of the repository ...


    // --- User-related functions (Unchanged) ---

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    suspend fun loginUser(name: String, pass: String): User? {
        return userDao.loginUser(name, pass)
    }


    // --- MODIFICATION 3: ADD Booking-related functions ---

    /**
     * Retrieves all bookings made by a specific user.
     */
    fun getBookingsForUser(userId: Int): Flow<List<Booking>> {
        return bookingDao.getBookingsByUserId(userId)
    }
}
