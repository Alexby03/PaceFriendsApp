package com.kth.stepapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kth.stepapp.data.repositories.PlayerRepository
import com.kth.stepapp.ui.theme.StepAppTheme
import com.kth.stepapp.ui.viewmodels.FakeHomeVM
import com.kth.stepapp.ui.viewmodels.HomeVM
import com.kth.stepapp.ui.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: HomeViewModel,
    onGoToActivity: (String) -> Unit,
    onGoToCalendar: () -> Unit,
    onGoToProfile: () -> Unit,
    onLogout: () -> Unit,
    onGoToLeaderBoard: () -> Unit
) {

    val fullName by vm.fullName.collectAsState()
    val weeklySteps by vm.weeklySteps.collectAsState()
    val weeklyScore by vm.weeklyScore.collectAsState()
    val streak by vm.streak.collectAsState()
    val dailyComplete by vm.daylilyComplete.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Home") },
                actions = {
                    IconButton(onClick = onGoToProfile) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile"
                        )
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

            Text(
                text = "Welcome back, $fullName",
                style = MaterialTheme.typography.headlineSmall
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("This week so far", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Steps: $weeklySteps")
                    Text("Score: $weeklyScore")
                    Text("Streak: $streak days")
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor =
                        if (dailyComplete)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (dailyComplete)
                            "Daily goal completed"
                        else
                            "Daily goal not completed yet",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Text("What would you like to do today?")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = { onGoToActivity("Walking") }) {
                    Text("Walking")
                }
                OutlinedButton(onClick = { onGoToActivity("Jogging") }) {
                    Text("Jogging")
                }
                OutlinedButton(onClick = { onGoToActivity("Running") }) {
                    Text("Running")
                }
            }

            Divider(thickness = 2.dp)


            Button(
                onClick = onGoToCalendar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calendar")
            }

            Spacer(modifier = Modifier.height(70.dp))

            Button(
                onClick = onGoToLeaderBoard,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Leaderboard")
            }

            Divider(thickness = 2.dp)

            Spacer(modifier = Modifier.weight(1f))


            OutlinedButton(
                onClick = {
                    vm.logoutPlayer()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    StepAppTheme {
        HomeScreen(
            vm = FakeHomeVM(),
            onGoToActivity = {},
            onGoToCalendar = {},
            onGoToProfile = {},
            onLogout = {},
            onGoToLeaderBoard = {}
        )
    }
}