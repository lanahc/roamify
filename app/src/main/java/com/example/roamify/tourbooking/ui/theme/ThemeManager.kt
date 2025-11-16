package com.example.roamify.tourbooking.ui.theme

import androidx.compose.runtime.mutableStateOf

/**
 * A simple object to manage the theme state (Light/Dark) for the entire app.
 */
object ThemeManager {
    /**
     * The state that determines if the app is in dark mode.
     * Using mutableStateOf ensures that UI components will automatically
     * update when this value changes.
     */
    val isDarkTheme = mutableStateOf(false) // Default to light mode

    /**
     * Toggles the current theme between light and dark.
     */
    fun toggleTheme() {
        isDarkTheme.value = !isDarkTheme.value
    }
}
