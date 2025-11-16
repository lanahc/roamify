package com.example.roamify.tourbooking.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roamify.tourbooking.ui.TourViewModelFactory

@Composable
fun SignUpScreen(
    factory: TourViewModelFactory,
    // This is the navigation action that AppNavigation.kt will provide.
    // It's called when the user successfully signs up or clicks "Already have an account?".
    onLoginClicked: () -> Unit
) {
    val viewModel: AuthViewModel = viewModel(factory = factory)
    val authResult by viewModel.authResult.observeAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // 1. ADD a state holder for the confirm password field
    var confirmPassword by remember { mutableStateOf("") }

    // This will trigger the navigation when the signup is successful
    LaunchedEffect(authResult) {
        if (authResult is AuthResult.Success) {
            onLoginClicked()
            viewModel.resetAuthResult() // Reset for next time
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 2. ADD the "Confirm Password" text field to the UI
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Display error messages from the ViewModel
        if (authResult is AuthResult.Error) {
            Text(
                text = (authResult as AuthResult.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            // 3. PASS the new confirmPassword value to the viewModel function
            onClick = { viewModel.signUpUser(name, email, password, confirmPassword) },
            modifier = Modifier.fillMaxWidth(),
            // 4. UPDATE the button's enabled state to check all fields
            enabled = name.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
        ) {
            Text("Sign Up")
        }

        TextButton(
            onClick = onLoginClicked
        ) {
            Text("Already have an account? Login")
        }
    }
}
