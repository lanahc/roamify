package com.example.roamify.tourbooking.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // <-- ADD THIS IMPORT
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roamify.tourbooking.ui.TourViewModelFactory
import com.example.roamify.tourbooking.ui.admin.AdminScreen
import com.example.roamify.tourbooking.ui.admin.BookingManagementScreen
import com.example.roamify.tourbooking.ui.auth.LoginScreen
import com.example.roamify.tourbooking.ui.auth.SignUpScreen
import com.example.roamify.tourbooking.ui.booking.BookingScreen
import com.example.roamify.tourbooking.ui.home.HomeScreen
import com.example.roamify.tourbooking.ui.user.UserScreen

@Composable
fun AppNavHost(factory: TourViewModelFactory) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.HOME) {

        // ... (HomeScreen, LoginScreen, SignUpScreen, UserScreen, AdminScreen, BookingManagementScreen composables remain the same) ...

        composable(AppRoutes.HOME) {
            HomeScreen(
                onLoginClicked = { navController.navigate(AppRoutes.LOGIN) },
                onSignUpClicked = { navController.navigate(AppRoutes.SIGN_UP) }
            )
        }

        composable(AppRoutes.LOGIN) {
            // My previous incorrect suggestion - this was likely already correct in your code
            LoginScreen(
                factory = factory, // This needs to be here
                onLoginSuccess = { isAdmin ->
                    val route = if (isAdmin) AppRoutes.ADMIN_DASHBOARD else AppRoutes.USER_DASHBOARD
                    navController.navigate(route) {
                        popUpTo(AppRoutes.HOME) { inclusive = true }
                    }
                },
                onSignUpClicked = {
                    navController.navigate(AppRoutes.SIGN_UP) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.SIGN_UP) {
            SignUpScreen(
                factory = factory,
                onLoginClicked = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.SIGN_UP) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.USER_DASHBOARD) {
            UserScreen(
                factory = factory,
                navigateToBookingForm = { tourId ->
                    navController.navigate("booking_form/$tourId")
                }
            )
        }

        composable(AppRoutes.ADMIN_DASHBOARD) {
            AdminScreen(factory = factory, navController = navController)
        }

        composable(route = AppRoutes.BOOKING_MANAGEMENT) {
            BookingManagementScreen(factory = factory)
        }

        // --- THE REAL FIX IS HERE ---
        composable(route = AppRoutes.BOOKING_FORM) { backStackEntry ->
            val tourId = backStackEntry.arguments?.getString("tourId")?.toLongOrNull()

            if (tourId != null) {
                // Replace navController.context with LocalContext.current
                val bookingFactory = TourViewModelFactory(LocalContext.current, tourId)
                BookingScreen(
                    factory = bookingFactory,
                    onBookingSuccess = {
                        navController.popBackStack()
                    }
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: Tour ID not found.", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}
