package com.kth.stepapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kth.stepapp.ui.viewmodels.FakeProfileVM
import com.kth.stepapp.ui.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    vm: ProfileViewModel,
    onBack: () -> Unit
) {
    val fullName by vm.fullName.collectAsStateWithLifecycle()
    val email by vm.email.collectAsStateWithLifecycle()
    val age by vm.age.collectAsStateWithLifecycle()
    val height by vm.heightCm.collectAsStateWithLifecycle()
    val weight by vm.weightKg.collectAsStateWithLifecycle()
    val gender by vm.gender.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = fullName ?: "Unnamed user",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Divider(
                        thickness = 3.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Text(
                        text = email ?: "No email set",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    ProfileItem("Age", age?.toString())
                    Divider(
                        thickness = 3.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    ProfileItem("Height", height?.let { "$it cm" })
                    Divider(
                        thickness = 3.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    ProfileItem("Weight", weight?.let { "$it kg" })
                    Divider(
                        thickness = 3.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    ProfileItem("Gender", gender)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = vm::onEditProfile,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit profile")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        vm = FakeProfileVM(),
        onBack = {}
    )
}

@Composable
private fun ProfileItem(
    label: String,
    value: String?
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value ?: "Not set",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
