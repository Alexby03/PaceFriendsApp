package com.kth.stepapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kth.stepapp.ui.viewmodels.FakeLoginVM
import com.kth.stepapp.ui.viewmodels.LoginViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
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

    var isRegisteringMode by remember { mutableStateOf(false) }

    LaunchedEffect(isSaved) {
        if (isSaved) onSuccess()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (isRegisteringMode) "Create Profile" else "Welcome Back") },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = isRegisteringMode,
                label = "AuthSwitch",
                transitionSpec = {
                    fadeIn() + slideInVertically { it / 2 } with fadeOut() + slideOutVertically { -it / 2 }
                }
            ) { isRegistering ->
                if (isRegistering) {
                    RegisterContent(
                        vm = vm,
                        fullName = fullName,
                        email = email,
                        password = password,
                        age = age,
                        heightCm = heightCm,
                        weightKg = weightKg,
                        gender = gender,
                        onSwitchToLogin = { isRegisteringMode = false }
                    )
                } else {
                    LoginContent(
                        vm = vm,
                        email = email,
                        password = password,
                        onSwitchToRegister = { isRegisteringMode = true }
                    )
                }
            }
            
            error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun LoginContent(
    vm: LoginViewModel,
    email: String,
    password: String,
    onSwitchToRegister: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = vm::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = password,
            onValueChange = vm::onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Button(
            onClick = vm::onLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        TextButton(onClick = onSwitchToRegister) {
            Text("Don't have an account? Create Profile")
        }
    }
}

@Composable
fun RegisterContent(
    vm: LoginViewModel,
    fullName: String,
    email: String,
    password: String,
    age: String,
    heightCm: String,
    weightKg: String,
    gender: String,
    onSwitchToLogin: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = fullName,
            onValueChange = vm::onFullNameChange,
            label = { Text("Full name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = vm::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = password,
            onValueChange = vm::onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = age,
                onValueChange = { if (it.all { c -> c.isDigit() }) vm.onAgeChange(it) },
                label = { Text("Age") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = gender,
                onValueChange = vm::onGenderChange,
                label = { Text("Gender") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = heightCm,
                onValueChange = { if (it.matches(Regex("""\d*\.?\d*"""))) vm.onHeightChange(it) },
                label = { Text("Height (cm)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            OutlinedTextField(
                value = weightKg,
                onValueChange = { if (it.matches(Regex("""\d*\.?\d*"""))) vm.onWeightChange(it) },
                label = { Text("Weight (kg)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }

        Button(
            onClick = vm::onRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Profile")
        }

        TextButton(onClick = onSwitchToLogin) {
            Text("Already have an account? Login")
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
