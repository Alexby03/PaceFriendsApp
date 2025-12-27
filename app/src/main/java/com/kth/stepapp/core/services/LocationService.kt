package com.kth.stepapp.core.services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationService(
    private val context: Context,
    private val client: FusedLocationProviderClient
) {
    @SuppressLint("MissingPermission")
    fun getLocationUpdates(intervalMs: Long): Flow<Location> = callbackFlow {
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(location)
                }
            }
        }

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            intervalMs
        ).build()

        client.requestLocationUpdates(
            request,
            callback,
            Looper.getMainLooper()
        )

        awaitClose {
            client.removeLocationUpdates(callback)
        }
    }
}

data class LocationUiState(
    val currentLat: Double = 0.0,
    val currentLng: Double = 0.0,
    val pathPoints: List<Pair<Double, Double>> = emptyList()
)

class MapVM(private val locationService: LocationService) : ViewModel() {

    private val _state = MutableStateFlow(LocationUiState())
    val state = _state.asStateFlow()

    fun startTracking() {
        viewModelScope.launch {
            locationService.getLocationUpdates(intervalMs = 1000)
                .collect { location ->
                    val newPoint = location.latitude to location.longitude

                    _state.update { oldState ->
                        oldState.copy(
                            currentLat = location.latitude,
                            currentLng = location.longitude,
                            pathPoints = oldState.pathPoints + newPoint
                        )
                    }
                }
        }
    }
}