package com.kth.stepapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kth.stepapp.ui.theme.StepAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGoToDemo: () -> Unit,
    onGoToLogin: () -> Unit,
    onGoToCalendar: () -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Home") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Home Screen")

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onGoToLogin) {
                Text("Login / Create Profile")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onGoToDemo) {
                Text("Go to Demo")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onGoToCalendar) {
                Text("Open Calendar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    StepAppTheme {
        HomeScreen(
            onGoToDemo = {},
            onGoToLogin = {},
            onGoToCalendar = {}
        )
    }
}

