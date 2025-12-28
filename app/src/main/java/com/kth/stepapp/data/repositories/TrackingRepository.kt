package com.kth.stepapp.data.repositories

import com.kth.stepapp.core.entities.LocationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object TrackingRepository {

    private val _nrOfSteps = MutableStateFlow(0L)
    private val _walkingTimeSeconds = MutableStateFlow(0L)
    private val _isTracking = MutableStateFlow(false)
    private val _caloriesBurned = MutableStateFlow(0)
    private val _locationUiState = MutableStateFlow(LocationUiState())

    val nrOfSteps = _nrOfSteps.asStateFlow()
    val walkingTimeSeconds = _walkingTimeSeconds.asStateFlow()
    val isTracking = _isTracking.asStateFlow()
    val caloriesBurned = _caloriesBurned.asStateFlow()
    val locationUiState = _locationUiState.asStateFlow()

    fun setTrackingState(isTracking: Boolean) {
        _isTracking.value = isTracking
    }

    fun updateMetrics(steps: Long, calories: Int) {
        _nrOfSteps.value = steps
        _caloriesBurned.value = calories
    }

    fun tickTimer() {
        _walkingTimeSeconds.update { it + 1 }
    }

    fun addLocationPoint(lat: Double, lng: Double) {
        _locationUiState.update { oldState ->
            oldState.copy(
                currentLat = lat,
                currentLng = lng,
                pathPoints = oldState.pathPoints + (lat to lng)
            )
        }
    }

    fun reset() {
        _nrOfSteps.value = 0L
        _walkingTimeSeconds.value = 0L
        _caloriesBurned.value = 0
        _locationUiState.value = LocationUiState()
    }
}
