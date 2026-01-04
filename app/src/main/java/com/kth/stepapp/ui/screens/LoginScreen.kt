package com.kth.stepapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kth.stepapp.ui.viewmodels.FakeLoginVM
import com.kth.stepapp.ui.viewmodels.LoginViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    vm: LoginViewModel,
    onSuccess: () -> Unit,
    onBack: () -> Unit
) {

    val fullName by vm.fullName.collectAsStateWithLifecycle()
    val email by vm.email.collectAsStateWithLifecycle()
    val password by vm.password.collectAsStateWithLifecycle()
    val age by vm.age.collectAsStateWithLifecycle()
    val heightCm by vm.heightCm.collectAsStateWithLifecycle()
    val weightKg by vm.weightKg.collectAsStateWithLifecycle()
    val gender by vm.gender.collectAsStateWithLifecycle()
    val isSaved by vm.isSaved.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    LaunchedEffect(isSaved) {
        if (isSaved) onSuccess()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = fullName,
                onValueChange = vm::onFullNameChange,
                label = { Text("Full name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true ,
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.primary)
            )

            OutlinedTextField(
                value = email,
                onValueChange = vm::onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.primary)
            )

            OutlinedTextField(
                value = password,
                onValueChange = vm::onPasswordChange,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.primary)
            )

            OutlinedTextField(
                value = age,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                        vm.onAgeChange(newValue)
                    }
                },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.primary)
            )

            OutlinedTextField(
                value = heightCm,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() ||
                        newValue.matches(Regex("""\d*\.?\d*"""))
                    ) {
                        vm.onHeightChange(newValue)
                    }
                },
                label = { Text("Height (cm)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.primary)
            )

            OutlinedTextField(
                value = weightKg,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() ||
                        newValue.matches(Regex("""\d*\.?\d*"""))
                    ) {
                        vm.onWeightChange(newValue)
                    }
                },
                label = { Text("Weight (kg)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.primary),)

            OutlinedTextField(
                value = gender,
                onValueChange = vm::onGenderChange,
                label = { Text("Gender") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.primary),
            )

            error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = vm::onSubmit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Profile")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        vm = FakeLoginVM(),
        onSuccess = {},
        onBack = {}
    )
}


