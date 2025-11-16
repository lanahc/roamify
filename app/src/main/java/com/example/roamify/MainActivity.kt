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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.roamify.RoamifyApplication
import com.example.roamify.tourbooking.ui.TourViewModelFactory
import com.example.roamify.tourbooking.ui.admin.AdminScreen // <-- Import your AdminScreen
import com.example.roamify.tourbooking.ui.theme.ThemeManager
import com.example.roamify.tourbooking.ui.user.UserScreen   // <-- Import your UserScreen
import com.example.roamify.ui.theme.RoamifyTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue

import com.example.roamify.tourbooking.navigation.AppNavHost
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
            val isDark by ThemeManager.isDarkTheme
            RoamifyTheme (darkTheme = isDark) {
                // Surface provides a background color for the app, using the theme's color scheme.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // The AppNavHost is now the single entry point for all of our app's UI.
                    // It will handle showing the home, login, user, and admin screens.
                    AppNavHost(factory = factory)
                }
            }
        }
    }
}






