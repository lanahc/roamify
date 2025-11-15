package com.example.roamify.tourbooking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.roamify.RoamifyApplication
import com.example.roamify.tourbooking.ui.TourViewModelFactory
import com.example.roamify.tourbooking.ui.admin.AdminScreen // <-- Import your AdminScreen
import com.example.roamify.tourbooking.ui.user.UserScreen   // <-- Import your UserScreen
import com.example.roamify.ui.theme.RoamifyTheme

// --- APPLICATION SETUP (Remains the same) ---

val ComponentActivity.app: RoamifyApplication
    get() = (application as RoamifyApplication)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // The factory is created once and passed down. This is good practice.
        val factory = app.viewModelFactory

        setContent {
            RoamifyTheme {
                MainApp(factory)
            }
        }
    }
}

// Enum for managing the current screen state
enum class Screen {
    USER, ADMIN
}

// --- MAIN APP COMPOSABLE (Handles Navigation) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(factory: TourViewModelFactory) {
    // State to track which screen is currently visible
    var currentScreen by remember { mutableStateOf(Screen.ADMIN) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Roamify Tour Booking") },
                actions = {
                    // Navigation buttons
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues) // Use paddingValues from the Scaffold
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // The "when" block now acts as a router, selecting which screen composable to display.
            when (currentScreen) {
                Screen.USER -> UserScreen(factory)
                Screen.ADMIN -> AdminScreen(factory)
            }
        }
    }
}
