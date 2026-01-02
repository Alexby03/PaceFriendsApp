package com.kth.stepapp.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.kth.stepapp.ui.theme.StepAppTheme
import com.kth.stepapp.ui.viewmodels.DemoViewModel
import com.kth.stepapp.ui.viewmodels.FakeDemoVM
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScreen(
    vm: DemoViewModel,
    onBack: () -> Unit
) {
    val steps by vm.nrOfSteps.collectAsState()
    val calories by vm.caloriesBurned.collectAsState()
    val timeSeconds by vm.walkingTimeSeconds.collectAsState()
    val isTracking by vm.isTracking.collectAsState(initial = false)
    val locationState by vm.locationUiState.collectAsState()
    val currentArea by vm.areaInSqMeters.collectAsState()

    val minutes = timeSeconds / 60
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    var hasPermissions by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasPermissions = granted
        if (!granted) Log.d("DemoScreen", "Permissions denied")
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            hasPermissions = true
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACTIVITY_RECOGNITION,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Test Screen") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Steps:", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(12.dp))
                    Text(steps.toString(), fontSize = 32.sp)
                }

                Spacer(Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Calories:", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(12.dp))
                    Text("$calories kcal", fontSize = 28.sp)
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Time walking: $minutes min ${timeSeconds % 60} s",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(24.dp))

                if (!hasPermissions) {
                    Button(onClick = {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACTIVITY_RECOGNITION,
                                Manifest.permission.POST_NOTIFICATIONS
                            )
                        )
                    }) {
                        Text("Enable Permissions")
                    }
                } else {
                    Button(
                        onClick = {
                            if (isTracking) vm.stopTracking() else vm.startTracking()
                        },
                        colors = if (isTracking)
                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        else ButtonDefaults.buttonColors()
                    ) {
                        Text(if (isTracking) "Stop Tracking" else "Start Tracking")
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "$currentArea m²",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                if (isPreview) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("OSMDroid Map")
                    }
                } else {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            MapView(ctx).apply {
                                setTileSource(TileSourceFactory.MAPNIK)
                                setMultiTouchControls(true)
                                controller.setZoom(18.0)
                            }
                        },
                        update = { map ->
                            if (locationState.currentLat != 0.0) {
                                map.controller.setCenter(
                                    GeoPoint(
                                        locationState.currentLat,
                                        locationState.currentLng
                                    )
                                )
                            }

                            map.overlays.clear()

                            val polyline = Polyline().apply {
                                outlinePaint.color = android.graphics.Color.RED
                                outlinePaint.strokeWidth = 10f
                                setPoints(
                                    locationState.pathPoints.map {
                                        GeoPoint(it.first, it.second)
                                    }
                                )
                            }

                            map.overlays.add(polyline)
                            map.invalidate()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DemoScreenPreview() {
    StepAppTheme {
        DemoScreen(
            vm = FakeDemoVM(),
            onBack = {}
        )
    }
}
