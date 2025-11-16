package com.example.roamify.tourbooking.ui.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roamify.tourbooking.ui.TourViewModelFactory

@Composable
fun BookingScreen(factory: TourViewModelFactory, onBookingSuccess: () -> Unit) {
    // Create the ViewModel using the factory
    val viewModel: BookingViewModel = viewModel(factory = factory)

    // Observe the tour details and UI state from the ViewModel
    val tour by viewModel.tour.observeAsState()
    val uiState by viewModel.uiState.observeAsState()

    // Local state for the form inputs
    var peopleCountInput by remember { mutableStateOf("1") }
    var stayDurationInput by remember { mutableStateOf("0") }
    var wantsCar by remember { mutableStateOf(false) }

    // This will run when the booking is successful
    LaunchedEffect(uiState) {
        if (uiState == BookingUiState.SUCCESS) {
            onBookingSuccess()
        }
    }

    // Main layout
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            BookingUiState.SUCCESS -> {
                // Show a success message
                SuccessDialog(message = "Booking Successful!", onDismiss = onBookingSuccess)
            }
            BookingUiState.SOLD_OUT -> {
                // Show a sold-out error
                ErrorDialog(message = "Sorry, this tour is sold out or has insufficient slots.", onDismiss = { onBookingSuccess() })
            }
            BookingUiState.ERROR -> {
                // Show a generic error
                ErrorDialog(message = "An unknown error occurred. Please try again.", onDismiss = { onBookingSuccess() })
            }
            else -> {
                // Show the main form
                if (tour == null) {
                    // Show a loading indicator while tour details are fetched
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        Text(tour!!.name, style = MaterialTheme.typography.headlineLarge)
                        Spacer(Modifier.height(8.dp))
                        Text(tour!!.location, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Slots available: ${tour!!.availableSlots}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Divider(modifier = Modifier.padding(vertical = 16.dp))

                        // Number of people
                        OutlinedTextField(
                            value = peopleCountInput,
                            onValueChange = { peopleCountInput = it.filter { char -> char.isDigit() } },
                            label = { Text("Number of People") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))

                        // Car Rental
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = wantsCar, onCheckedChange = { wantsCar = it })
                            Text("Include Car Rental?")
                        }
                        Spacer(Modifier.height(16.dp))

                        // Hotel Stay
                        OutlinedTextField(
                            value = stayDurationInput,
                            onValueChange = { stayDurationInput = it.filter { char -> char.isDigit() } },
                            label = { Text("Duration of Hotel Stay (in days)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(24.dp))

                        // Confirm Button
                        Button(
                            onClick = {
                                val peopleCount = peopleCountInput.toIntOrNull() ?: 1
                                val stayDuration = stayDurationInput.toIntOrNull() ?: 0
                                // User ID is hardcoded to 1 for now. In a real app, this would come from a user session.
                                viewModel.confirmBooking(1, peopleCount, wantsCar, stayDuration)
                            },
                            enabled = uiState != BookingUiState.LOADING,
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            if (uiState == BookingUiState.LOADING) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                            } else {
                                Text("Confirm Booking")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Simple composable for a success dialog
@Composable
fun SuccessDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Success") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK") }
        }
    )
}

// Simple composable for an error dialog
@Composable
fun ErrorDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Booking Failed") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK") }
        }
    )
}
