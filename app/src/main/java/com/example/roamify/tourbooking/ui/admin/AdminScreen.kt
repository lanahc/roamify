package com.example.roamify.tourbooking.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.navigation.NavController
import com.example.roamify.tourbooking.data.Tour
import com.example.roamify.tourbooking.navigation.AppRoutes // <-- IMPORT FOR NAVIGATION
import com.example.roamify.tourbooking.ui.TourViewModelFactory
import kotlin.math.min

private val DEFAULT_TOUR = Tour(tourId = 0, name = "", location = "", description = "", price = 0.0, maxCapacity = 0, availableSlots = 0)

@Composable
fun AdminScreen(factory: TourViewModelFactory, navController: NavController) {
    val viewModel: AdminViewModel = viewModel(factory = factory)
    val allTours by viewModel.allTours.observeAsState(initial = emptyList())
    val adminStatus by viewModel.adminStatus.observeAsState()
    var tourToEdit by remember { mutableStateOf<Tour?>(null) }
    val initialTour = tourToEdit ?: DEFAULT_TOUR
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp) // Add padding to the whole screen
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Admin Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        adminStatus?.let { status ->
            Text(
                "Status: $status",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 1. Tour Creation/Editing Form
        AddEditTourForm(
            viewModel = viewModel,
            initialTour = initialTour,
            onSubmissionSuccess = { tourToEdit = null }
        )

        // --- THIS IS THE ONLY ADDITION TO YOUR ORIGINAL STRUCTURE ---
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                // Navigate to the booking management screen
                navController.navigate(AppRoutes.BOOKING_MANAGEMENT)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Manage Bookings")
        }
        // --- END OF THE ADDITION ---

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        // 2. Management List Header
        Text(
            text = "Total Tours Managed: ${allTours.size}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        // 3. Management List
        TourManagementList(
            tours = allTours,
            viewModel = viewModel,
            onEditClicked = { tourToEdit = it }
        )
        // Add a final spacer for better layout at the bottom
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Your AddEditTourForm composable remains completely unchanged
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTourForm(
    viewModel: AdminViewModel,
    initialTour: Tour,
    onSubmissionSuccess: () -> Unit
) {
    val isEditMode = initialTour.tourId != 0L
    val submitButtonText = if (isEditMode) "Update Tour" else "Add Tour"

    var title by remember(initialTour.tourId) { mutableStateOf(initialTour.name) }
    var description by remember(initialTour.tourId) { mutableStateOf(initialTour.description) }
    var location by remember(initialTour.tourId) { mutableStateOf(initialTour.location) }
    var priceInput by remember(initialTour.tourId) { mutableStateOf(if (isEditMode) "%.2f".format(initialTour.price) else "") }
    var capacityInput by remember(initialTour.tourId) { mutableStateOf(if (isEditMode) initialTour.maxCapacity.toString() else "") }

    val isFormValid = title.isNotBlank() && description.isNotBlank() && location.isNotBlank() && priceInput.toDoubleOrNull() != null && capacityInput.toIntOrNull() != null

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(if (isEditMode) "Edit Tour (ID: ${initialTour.tourId})" else "Create New Tour", style = MaterialTheme.typography.titleLarge)
        if (isEditMode) {
            TextButton(onClick = onSubmissionSuccess) {
                Text("Cancel Edit / Add New")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            singleLine = false
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = priceInput,
                onValueChange = { priceInput = it.filter { char -> char.isDigit() || char == '.' } },
                label = { Text("Price (\$)", style = MaterialTheme.typography.bodySmall) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            OutlinedTextField(
                value = capacityInput,
                onValueChange = { capacityInput = it.filter { char -> char.isDigit() } },
                label = { Text("Capacity", style = MaterialTheme.typography.bodySmall) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val price = priceInput.toDoubleOrNull() ?: return@Button
                val capacity = capacityInput.toIntOrNull() ?: return@Button

                if (isEditMode) {
                    val updatedTour = initialTour.copy(
                        name = title,
                        description = description,
                        location = location,
                        price = price,
                        maxCapacity = capacity,
                        availableSlots = minOf(capacity, initialTour.availableSlots)
                    )
                    viewModel.updateTour(updatedTour)
                } else {
                    viewModel.addNewTour(
                        name = title,
                        description = description,
                        location = location,
                        price = price,
                        maxCapacity = capacity
                    )
                }
                onSubmissionSuccess()
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(submitButtonText)
        }
    }
}


// Your TourManagementList composable remains completely unchanged
@Composable
fun TourManagementList(tours: List<Tour>, viewModel: AdminViewModel, onEditClicked: (Tour) -> Unit) {
    if (tours.isEmpty()) {
        Text("No tours have been created yet.", style = MaterialTheme.typography.bodySmall)
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp, max = 500.dp)
            .padding(top = 8.dp)
    ) {
        items(tours, key = { it.tourId }) { tour ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(tour.name, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Slots: ${tour.availableSlots}/${tour.maxCapacity} | Price: \$${"%.2f".format(tour.price)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = { onEditClicked(tour) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }

                    IconButton(onClick = { viewModel.deleteTour(tour) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}
