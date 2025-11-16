package com.example.roamify.tourbooking.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roamify.tourbooking.data.Booking
import com.example.roamify.tourbooking.data.BookingStatus
import com.example.roamify.tourbooking.ui.TourViewModelFactory

@OptIn(ExperimentalMaterial3Api::class) // <-- ADD THIS ANNOTATION
@Composable
fun BookingManagementScreen(factory: TourViewModelFactory) {
    val viewModel: BookingManagementViewModel = viewModel(factory = factory)
    val bookings by viewModel.allBookings.observeAsState(initial = emptyList())

    Scaffold(
        topBar = {
            // No changes needed inside here, but the OptIn is required
            TopAppBar(
                title = { Text("Booking Management") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (bookings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No bookings have been made yet.")
            }
        } else {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    items(bookings, key = { it.bookingId }) { booking ->
                        BookingCard(
                            booking = booking,
                            onConfirm = { viewModel.confirmBooking(booking) },
                            onCancel = { viewModel.cancelBooking(booking) }
                        )
                    }
                }
            )
        }
    }
}


@Composable
fun BookingCard(booking: Booking, onConfirm: () -> Unit, onCancel: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Booking #${booking.bookingId}", style = MaterialTheme.typography.titleLarge)
                StatusChip(status = booking.status)
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            BookingDetailRow("Tour ID:", booking.tourId.toString())
            BookingDetailRow("User ID:", booking.userId.toString())
            BookingDetailRow("Guests:", booking.numberOfPeople.toString())
            BookingDetailRow("Car Rental:", if (booking.requiresCarRental) "Yes" else "No")
            BookingDetailRow("Hotel Stay:", "${booking.stayDurationInDays} days")
            BookingDetailRow("Date:", booking.bookingDate)

            // Only show action buttons if the booking is PENDING
            if (booking.status == BookingStatus.PENDING) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Green
                    ) {
                        Text("Confirm")
                    }
                    Button(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
fun BookingDetailRow(label: String, value: String) {
    Row {
        Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
        Text(value)
    }
}

@Composable
fun StatusChip(status: BookingStatus) {
    val color = when (status) {
        BookingStatus.PENDING -> MaterialTheme.colorScheme.secondaryContainer
        BookingStatus.CONFIRMED -> Color(0xFF4CAF50) // Green
        BookingStatus.CANCELLED -> MaterialTheme.colorScheme.errorContainer
    }
    val textColor = when (status) {
        BookingStatus.PENDING -> MaterialTheme.colorScheme.onSecondaryContainer
        BookingStatus.CONFIRMED -> Color.White
        BookingStatus.CANCELLED -> MaterialTheme.colorScheme.onErrorContainer
    }
    SuggestionChip(
        onClick = { /* No action */ },
        label = { Text(status.name) }, // The textColor is now handled by the 'colors' parameter

        // --- THIS IS THE FIX ---
        // Use the ChipDefaults.chipColors() factory function to create the colors object.
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = color,
            labelColor = textColor
        )
    )
}

