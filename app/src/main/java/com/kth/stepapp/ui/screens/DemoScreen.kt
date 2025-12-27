package com.kth.stepapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kth.stepapp.ui.theme.StepAppTheme
import com.kth.stepapp.ui.viewmodels.DemoVM
import com.kth.stepapp.ui.viewmodels.DemoViewModel
import com.kth.stepapp.ui.viewmodels.FakeDemoVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScreen(
    vm: DemoViewModel
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Test Screen") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Hello World!"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DemoScreenPreview() {
    StepAppTheme {
        DemoScreen(vm = FakeDemoVM())
    }
}