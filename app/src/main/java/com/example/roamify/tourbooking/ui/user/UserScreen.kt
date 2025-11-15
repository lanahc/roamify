package com.example.roamify.tourbooking.ui.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roamify.tourbooking.data.Tour
import com.example.roamify.tourbooking.ui.TourViewModelFactory

@Composable
fun UserScreen(factory: TourViewModelFactory) {
    val viewModel: UserViewModel = viewModel(factory = factory)
    val availableTours by viewModel.availableTours.observeAsState(initial = emptyList())
    val bookingStatus by viewModel.bookingStatus.observeAsState()

    // This outer Column now correctly manages the layout for scrolling.
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Welcome, Traveler!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // Display Status Message
        bookingStatus?.let { status ->
            Text(
                "Status: $status",
                color = if (status.contains("successful")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (availableTours.isEmpty()) {
            Text("No tours currently available for booking. Check back later!", style = MaterialTheme.typography.bodyLarge)
        } else {
            Text("Available Tours (${availableTours.size})", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // The LazyColumn now uses weight(1f) to fill the remaining space and enable scrolling.
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(availableTours, key = { it.tourId }) { tour ->
                    TourCard(tour = tour, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun TourCard(tour: Tour, viewModel: UserViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title and Description
            Text(
                tour.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                tour.description.take(100) + if (tour.description.length > 100) "..." else "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            // Details and Booking Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Price: \$${"%.2f".format(tour.price)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    val slotsMessage = if (tour.availableSlots > 5) {
                        "High Availability"
                    } else if (tour.availableSlots > 0) {
                        "Only ${tour.availableSlots} spots left!"
                    } else {
                        "Sold Out"
                    }
                    Text(
                        slotsMessage,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (tour.availableSlots <= 5 && tour.availableSlots > 0) MaterialTheme.colorScheme.error else LocalContentColor.current
                    )
                }

                Button(
                    onClick = { viewModel.attemptBooking(tour.tourId) },
                    enabled = tour.availableSlots > 0,
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(if (tour.availableSlots > 0) "Book Now" else "Sold Out")
                }
            }
        }
    }
}
