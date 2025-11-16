package com.example.roamify.tourbooking.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roamify.tourbooking.ui.TourViewModelFactory
import com.example.roamify.tourbooking.ui.admin.AdminScreen
import com.example.roamify.tourbooking.ui.auth.LoginScreen
import com.example.roamify.tourbooking.ui.auth.SignUpScreen
import com.example.roamify.tourbooking.ui.home.HomeScreen
import com.example.roamify.tourbooking.ui.user.UserScreen

/**
 * Defines the unique string constants (routes) for each screen in the app.
 */
object AppRoutes {
    const val HOME = "home"
    const val LOGIN = "login"
    const val SIGN_UP = "signup"
    const val USER_DASHBOARD = "user_dashboard"
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val BOOKING_FORM = "booking_form/{tourId}"
}

@Composable
fun AppNavHost(factory: TourViewModelFactory) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.HOME) {

        composable(AppRoutes.HOME) {
            HomeScreen(
                onLoginClicked = { navController.navigate(AppRoutes.LOGIN) },
                onSignUpClicked = { navController.navigate(AppRoutes.SIGN_UP) }
            )
        }

        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { isAdmin ->
                    val route = if (isAdmin) AppRoutes.ADMIN_DASHBOARD else AppRoutes.USER_DASHBOARD
                    navController.navigate(route) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                    }
                },
                // Navigate to sign up, but pop login off the stack
                onSignUpClicked = {
                    navController.navigate(AppRoutes.SIGN_UP) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

       composable(AppRoutes.SIGN_UP) {
            SignUpScreen(
                factory = factory, // <-- FIX: Pass the factory to the SignUpScreen
                onLoginClicked = {
                    navController.navigate(AppRoutes.LOGIN) {
                        // Pop the sign-up screen off the stack when navigating to login.
                        popUpTo(AppRoutes.SIGN_UP) { inclusive = true }
                    }
                }
            )
        }


        // **FIX 3: Correct the UserScreen composable call**
        composable(AppRoutes.USER_DASHBOARD) {
            UserScreen(
                factory = factory,
                // Define the navigation action for booking a tour
                navigateToBookingForm = { tourId ->
                    // Build the route string with the actual tour ID
                    navController.navigate("booking_form/$tourId")
                }
            )
        }

        composable(AppRoutes.ADMIN_DASHBOARD) {
            AdminScreen(factory)
        }

        // **FIX 4: Add the new destination for the Booking Form**
        composable(route = AppRoutes.BOOKING_FORM) { backStackEntry ->
            // Extract the tourId from the route arguments
            val tourId = backStackEntry.arguments?.getString("tourId")?.toLongOrNull()

            if (tourId != null) {
                // In our next step, we will create the real BookingScreen.
                // For now, this placeholder confirms our navigation is working.
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Booking form for Tour ID: $tourId",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            } else {
                // Show an error if the tourId is missing for any reason
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: Tour ID not found.", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}
