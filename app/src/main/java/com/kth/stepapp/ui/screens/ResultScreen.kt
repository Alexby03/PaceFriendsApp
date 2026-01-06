package com.kth.stepapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kth.stepapp.ui.viewmodels.FakeScoreAndMapVM
import com.kth.stepapp.ui.viewmodels.ResultViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    vm: ResultViewModel,
    onBack: () -> Unit
) {
    val steps by vm.steps.collectAsStateWithLifecycle()
    val time by vm.timeSeconds.collectAsStateWithLifecycle()
    val calories by vm.calories.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Workout result") },
                actions = {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Done")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // 🎉 Gratulationstext
                    Text(
                        text = "Great job!",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "You’ve completed your workout.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Divider(thickness = 2.dp)

                    // 👣 Steps
                    Text(
                        text = "Steps",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = steps.toString(),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Divider(thickness = 2.dp)

                    // 🔥 Calories
                    Text(
                        text = "Calories burned",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = calories.toString(),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Divider(thickness = 2.dp)

                    // ⏱ Time
                    Text(
                        text = "Time",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "${time / 60} min",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    ResultScreen(
        vm = FakeScoreAndMapVM(),
        onBack = {}
    )
}

