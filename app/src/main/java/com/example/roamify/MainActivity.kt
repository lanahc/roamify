package com.example.roamify.tourbooking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roamify.RoamifyApplication
import com.example.roamify.tourbooking.data.Tour // <-- ADDED: Imports the Tour data class
import com.example.roamify.tourbooking.ui.TourViewModelFactory
import com.example.roamify.tourbooking.ui.admin.AdminViewModel // <-- CORRECTED PATH
import com.example.roamify.tourbooking.ui.user.UserViewModel    // <-- CORRECTED PATH
import com.example.roamify.ui.theme.RoamifyTheme // Assuming theme is also under tourbooking


// Extension property to easily access the application instance
val ComponentActivity.app: RoamifyApplication
    get() = application as RoamifyApplication

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the factory instance created in the Application class
        val factory = app.viewModelFactory

        setContent {
            // Note: We need a placeholder for RoamifyTheme if it doesn't exist yet,
            // but we'll assume it's set up in the theme package.
            RoamifyTheme {
                // Pass the factory to the main UI composable
                MainScreen(factory)
            }
        }
    }
}

// Enum for simple navigation state
enum class Screen {
    USER, ADMIN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(factory: TourViewModelFactory) {
    // State to manage which screen is currently displayed
    var currentScreen by remember { mutableStateOf(Screen.USER) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Roamify Tour Booking") },
                actions = {
                    // Simple navigation buttons
                    TextButton(onClick = { currentScreen = Screen.USER }) {
                        Text("User View")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { currentScreen = Screen.ADMIN }) {
                        Text("Admin View")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (currentScreen) {
                Screen.USER -> UserScreen(factory)
                Screen.ADMIN -> AdminScreen(factory)
            }
        }
    }
}

// --- USER UI (Placeholder) ---
@Composable
fun UserScreen(factory: TourViewModelFactory) {
    // Instantiating the ViewModel using the factory and remembering the instance
    // Note: The use of viewModel() should automatically resolve if the Compose dependency is correct.
    val viewModel: UserViewModel = viewModel(factory = factory)
    // Observing LiveData from the ViewModel
    // Note: The observeAsState function should resolve if the LiveData Compose dependency is correct.
    val availableTours by viewModel.availableTours.observeAsState(initial = emptyList())
    val bookingStatus by viewModel.bookingStatus.observeAsState()

    Text("Welcome, Traveler!", style = MaterialTheme.typography.headlineMedium)
    Spacer(modifier = Modifier.height(16.dp))

    // Display Status Message
    bookingStatus?.let { status ->
        Text(
            "Status: $status",
            color = if (status.contains("successful")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    // Placeholder: Show tour count and simulate a booking
    Text("Available Tours: ${availableTours.size}", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    if (availableTours.isNotEmpty()) {
        // Now that Tour is imported, we can access its properties (title, tourId, etc.)
        val firstTour = availableTours.first()
        Card(
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(firstTour.title, style = MaterialTheme.typography.titleLarge)
                Text("Price: \$${firstTour.price} | Slots: ${firstTour.availableSlots}")
                Button(
                    onClick = { viewModel.attemptBooking(firstTour.tourId) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Book Tour")
                }
            }
        }
    } else {
        Text("No tours currently available. Admin needs to add tours.")
    }
}

// --- ADMIN UI (Placeholder) ---
@Composable
fun AdminScreen(factory: TourViewModelFactory) {
    // Instantiating the ViewModel using the factory and remembering the instance
    val viewModel: AdminViewModel = viewModel(factory = factory)
    // Observing LiveData from the ViewModel
    val allTours by viewModel.allTours.observeAsState(initial = emptyList())
    val adminStatus by viewModel.adminStatus.observeAsState()

    Text("Admin Dashboard", style = MaterialTheme.typography.headlineMedium)
    Spacer(modifier = Modifier.height(16.dp))

    // Display Status Message
    adminStatus?.let { status ->
        Text("Admin Status: $status", color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(8.dp))
    }

    Text("Total Tours Managed: ${allTours.size}", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    // Placeholder button to add a sample tour
    Button(onClick = {
        viewModel.addNewTour(
            title = "Sample Tour ${allTours.size + 1}",
            description = "A great automated tour.",
            capacity = 10,
            price = 99.99
        )
    }) {
        Text("Add Sample Tour")
    }

    // Placeholder: Simple list of tours
    Spacer(modifier = Modifier.height(16.dp))
    allTours.take(3).forEach { tour ->
        Card(
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(tour.title, style = MaterialTheme.typography.titleSmall)
                    Text("Slots: ${tour.availableSlots}/${tour.maxCapacity}", style = MaterialTheme.typography.bodySmall)
                }
                Text("ID: ${tour.tourId}", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

// Placeholder for the default Compose theme (assuming it exists)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Text("Preview cannot show full functionality without a live ViewModel instance.")
}