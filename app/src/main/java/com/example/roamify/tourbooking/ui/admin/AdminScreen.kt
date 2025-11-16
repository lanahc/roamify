package com.example.roamify.tourbooking.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState // <-- NEW IMPORT
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll // <-- NEW IMPORT
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roamify.tourbooking.data.Tour
import com.example.roamify.tourbooking.ui.TourViewModelFactory
import kotlin.math.min // Needed for minOf if not available by default

// Define a null Tour object to represent the 'Add' state
private val DEFAULT_TOUR = Tour(tourId = 0, name = "", location = "", description = "", price = 0.0, maxCapacity = 0, availableSlots = 0)

// --- ADMIN DASHBOARD ---

@Composable
fun AdminScreen(factory: TourViewModelFactory) {
    val viewModel: AdminViewModel = viewModel(factory = factory)
    val allTours by viewModel.allTours.observeAsState(initial = emptyList())
    val adminStatus by viewModel.adminStatus.observeAsState()

    // State to hold the tour currently being edited. Null means ADD mode.
    var tourToEdit by remember { mutableStateOf<Tour?>(null) }

    // Ensure the edit mode uses the actual object if available, otherwise use default for Add mode
    val initialTour = tourToEdit ?: DEFAULT_TOUR

    // NEW: Create a ScrollState instance
    val scrollState = rememberScrollState()

    // CHANGE: Wrap the main content Column with the verticalScroll modifier
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // <-- ADDED SCROLLING HERE
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Admin Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // Display Status Message
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

        Spacer(modifier = Modifier.height(16.dp))
        Divider(modifier = Modifier.padding(horizontal = 16.dp)) // Add padding to divider
        Spacer(modifier = Modifier.height(16.dp))

        // 2. Management List Header
        Text(
            text = "Total Tours Managed: ${allTours.size}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 8.dp) // Add padding for alignment
        )

        // 3. Management List
        // CHANGE: Use a fixed height for the list to prevent it from consuming all remaining scroll space.
        // The outer Column is now scrollable, so the inner LazyColumn must be bounded.
        TourManagementList(
            tours = allTours,
            viewModel = viewModel,
            onEditClicked = { tourToEdit = it }
        )
    }
}

// ... (AddEditTourForm and TourManagementList implementations remain below)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTourForm(
    viewModel: AdminViewModel,
    initialTour: Tour,
    onSubmissionSuccess: () -> Unit
) {
    val isEditMode = initialTour.tourId != 0L
    val submitButtonText = if (isEditMode) "Update Tour" else "Add Tour"

    // --- State for each text field ---
    var title by remember(initialTour.tourId) { mutableStateOf(initialTour.name) }
    var description by remember(initialTour.tourId) { mutableStateOf(initialTour.description) }
    // FIX 1: Add state for the location field
    var location by remember(initialTour.tourId) { mutableStateOf(initialTour.location) }
    var priceInput by remember(initialTour.tourId) { mutableStateOf(if (isEditMode) "%.2f".format(initialTour.price) else "") }
    var capacityInput by remember(initialTour.tourId) { mutableStateOf(if (isEditMode) initialTour.maxCapacity.toString() else "") }

    // Form validation now includes the location field
    val isFormValid = title.isNotBlank() && description.isNotBlank() && location.isNotBlank() && priceInput.toDoubleOrNull() != null && capacityInput.toIntOrNull() != null

    Column(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().padding(bottom = 8.dp)) {
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
        // FIX 2: Add the missing location text field
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
                        location = location, // Also update location on edit
                        price = price,
                        maxCapacity = capacity,
                        // Logic to prevent available slots from exceeding new capacity
                        availableSlots = minOf(capacity, initialTour.availableSlots)
                    )
                    viewModel.updateTour(updatedTour)
                } else {
                    // FIX 3: Update the function call to match the new signature
                    viewModel.addNewTour(
                        name = title,
                        description = description,
                        location = location, // Pass the new location state
                        price = price,
                        maxCapacity = capacity
                    )
                }
                // Reset form fields after submission
                onSubmissionSuccess()
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(submitButtonText)
        }
    }
}



@Composable
fun TourManagementList(tours: List<Tour>, viewModel: AdminViewModel, onEditClicked: (Tour) -> Unit) {
    if (tours.isEmpty()) {
        Text("No tours have been created yet.", style = MaterialTheme.typography.bodySmall)
        return
    }

    // CHANGE: The LazyColumn must have a fixed height because its parent is now scrollable.
    // We use heightIn to give it a minimum height but allow it to expand for the content.
    LazyColumn(
        modifier = with(Modifier) {
            fillMaxWidth()
                .heightIn(min = 200.dp, max = 500.dp) // Set a reasonable fixed/max height
                .padding(start = 16.dp,end = 16.dp,top = 8.dp)
        }
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

                    // EDIT BUTTON
                    IconButton(onClick = { onEditClicked(tour) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }

                    // DELETE BUTTON
                    IconButton(onClick = { viewModel.deleteTour(tour) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}