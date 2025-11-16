package com.example.roamify.tourbooking.ui.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roamify.tourbooking.data.Tour
import com.example.roamify.tourbooking.ui.TourViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    factory: TourViewModelFactory,
    // This navigation action is provided from AppNavigation.kt
    navigateToBookingForm: (tourId: Long) -> Unit
) {
    val viewModel: UserViewModel = viewModel(factory = factory)
    val availableTours by viewModel.availableTours.observeAsState(initial = emptyList())

    // *** FIX 1: Observe the new navigation LiveData from the ViewModel ***
    // When its value changes, we trigger the navigation and then reset it.
    viewModel.navigateToBookingForm.observeAsState().value?.let { tourId ->
        navigateToBookingForm(tourId)      // Execute the navigation
        viewModel.onNavigationComplete() // Reset the event to prevent re-navigation
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Welcome, Traveler!") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            if (availableTours.isEmpty()) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("No tours currently available. Check back later!")
                }
            } else {
                Text(
                    "Available Tours (${availableTours.size})",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(availableTours, key = { it.tourId }) { tour ->
                        // Pass the ViewModel function to the card
                        TourCard(
                            tour = tour,
                            onBookClicked = { viewModel.onBookTourClicked(tour.tourId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TourCard(tour: Tour, onBookClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                tour.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            // Show the tour location
            Text(tour.location, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 4.dp))
            Text(
                tour.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Price: \$${"%.2f".format(tour.price)}", style = MaterialTheme.typography.titleMedium)
                    Text("Slots: ${tour.availableSlots}/${tour.maxCapacity}", style = MaterialTheme.typography.bodySmall)
                }

                // *** FIX 2: onClick now calls the passed-in lambda ***
                Button(
                    onClick = onBookClicked,
                    enabled = tour.availableSlots > 0,
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(if (tour.availableSlots > 0) "Book Now" else "Sold Out")
                }
            }
        }
    }
}
