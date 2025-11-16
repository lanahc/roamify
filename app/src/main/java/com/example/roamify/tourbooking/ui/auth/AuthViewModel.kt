package com.example.roamify.tourbooking.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roamify.tourbooking.data.TourRepository
import com.example.roamify.tourbooking.data.User
import kotlinx.coroutines.launch

// ======================================================================
// THIS IS THE MISSING CLASS DEFINITION THAT NEEDS TO BE ADDED
// ======================================================================
/**
 * A sealed class to represent the state of an authentication operation.
 * This is the class that AuthScreens.kt has been trying to use.
 */
sealed class AuthResult {
    data class Success(val isAdmin: Boolean) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
    object Neutral : AuthResult() // Represents the initial, idle state
}
// ======================================================================

/**
 * ViewModel responsible for the business logic of signing up and logging in.
 */
class AuthViewModel(private val repository: TourRepository) : ViewModel() {

    // The LiveData that the UI will observe. It holds the current authentication state.
    private val _authResult = MutableLiveData<AuthResult>(AuthResult.Neutral)
    val authResult: LiveData<AuthResult> = _authResult

    /**
     * Handles the user sign-up process.
     */
    fun signUpUser(name: String, phone: String, pass: String, confirmPass: String) {
        // --- Input Validation ---
        if (name.isBlank() || phone.isBlank() || pass.isBlank()) {
            _authResult.value = AuthResult.Error("All fields are required.")
            return
        }

        if (pass != confirmPass) {
            _authResult.value = AuthResult.Error("Passwords do not match.")
            return
        }

        // --- Database Operation ---
        _authResult.value = AuthResult.Loading
        viewModelScope.launch {
            // Check if a user with that name already exists (optional but good practice)
            // For now, we'll proceed directly.
            val user = User(name = name, phoneNumber = phone, password = pass)
            repository.insertUser(user)
            // We set the result to Success (always for a non-admin user).
            _authResult.value = AuthResult.Success(isAdmin = false)
        }
    }

    /**
     * Handles the user and admin login process.
     */
    fun loginUser(name: String, pass: String, isAdminLogin: Boolean) {
        // --- Input Validation ---
        if (name.isBlank() || pass.isBlank()) {
            _authResult.value = AuthResult.Error("Name and password cannot be empty.")
            return
        }

        // --- Admin Login Path ---
        if (isAdminLogin) {
            if (name.lowercase() == "admin" && pass == "admin123") {
                _authResult.value = AuthResult.Success(isAdmin = true)
            } else {
                _authResult.value = AuthResult.Error("Invalid admin credentials.")
            }
            return
        }

        // --- Simplified User Login Path (No Database) ---
        // For now, any non-empty name and password will work for a user.
        // Or you can use specific credentials like "user" and "pass".
        if (name.isNotEmpty() && pass.isNotEmpty()) {
            _authResult.value = AuthResult.Success(isAdmin = false)
        } else {
            // This case is already handled by the initial check, but is here for clarity.
            _authResult.value = AuthResult.Error("An unknown error occurred.")
        }

        _authResult.value = AuthResult.Loading
        viewModelScope.launch {
            val user = repository.loginUser(name, pass)
            if (user != null) {
                // User found in database, login is successful
                _authResult.value = AuthResult.Success(isAdmin = false)
            } else {
                // No user found with those credentials
                _authResult.value = AuthResult.Error("Invalid name or password. Please sign up.")
            }
        }
    }

    /**
     * Resets the authentication result state back to Neutral.
     * This is important to prevent stale success/error messages from reappearing.
     */
    fun resetAuthResult() {
        _authResult.value = AuthResult.Neutral
    }
}
