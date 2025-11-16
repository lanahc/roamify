package com.example.roamify.tourbooking.navigation

/**
 * Defines the unique string constants (routes) for each screen in the app.
 * This object provides a single, reliable source for all navigation routes,
 * preventing typos and making the code easier to manage.
 */
object AppRoutes {
    const val HOME = "home"
    const val LOGIN = "login"
    const val SIGN_UP = "signup"
    const val USER_DASHBOARD = "user_dashboard"
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val BOOKING_FORM = "booking_form/{tourId}"
    const val BOOKING_MANAGEMENT = "booking_management"
}
