package com.example.roamify.tourbooking.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.roamify.tourbooking.ui.theme.ThemeManager


// Add this annotation because TopAppBar is still considered experimental
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {
    // Scaffold provides the basic structure including a TopAppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Roamify") },
                actions = {
                    // This is the theme-switching icon button
                    val isDark by ThemeManager.isDarkTheme
                    IconButton(onClick = { ThemeManager.toggleTheme() }) {
                        Icon(
                            imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // The main content of your screen goes here
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Use the innerPadding provided by the Scaffold
                .padding(innerPadding)
                .padding(16.dp), // Add your own padding as well
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Welcome to Roamify!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Your next adventure starts here book a tour with us!",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = onLoginClicked,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onSignUpClicked,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }
        }
    }
}
