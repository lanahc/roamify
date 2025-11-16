package com.example.roamify.tourbooking.ui.admin

// Other imports like Column, Button, etc.
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController // <-- ADD THIS IMPORT
import com.example.roamify.tourbooking.navigation.AppRoutes
import com.example.roamify.tourbooking.ui.TourViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    factory: TourViewModelFactory,
    navController: NavController // <-- THIS PARAMETER IS NEW
) {
    val viewModel: AdminViewModel = viewModel(factory = factory)
    val tours by viewModel.allTours.observeAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            // Button Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Add Tour")
                }

                // This button will now work because 'navController' exists
                Button(
                    onClick = {
                        navController.navigate(AppRoutes.BOOKING_MANAGEMENT)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Manage Bookings")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tour List
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(tours) { tour ->
                    // Your TourItemCard or whatever you use to display tours
                    Text("Tour: ${tour.name} (ID: ${tour.tourId})") // Placeholder
                }
            }
        }

        if (showAddDialog) {
            // Your AddTourDialog composable
            // AddTourDialog(viewModel = viewModel, onDismiss = { showAddDialog = false })
        }
    }
}
