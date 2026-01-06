package com.kth.stepapp.data.repositories

import com.kth.stepapp.core.entities.LocationUiState
import com.kth.stepapp.core.utils.ScoreCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object TrackingRepository {

    private val _nrOfSteps = MutableStateFlow(0L)
    private val _walkingTimeSeconds = MutableStateFlow(0L)
    private val _isTracking = MutableStateFlow(false)
    private val _caloriesBurned = MutableStateFlow(0)
    private val _locationUiState = MutableStateFlow(LocationUiState())
    private val _areaInSqMeters: MutableStateFlow<Double> = MutableStateFlow(0.0)
    private val _score: MutableStateFlow<Long> = MutableStateFlow(0)
    private val _activity: MutableStateFlow<String> = MutableStateFlow("Walking")

    val nrOfSteps = _nrOfSteps.asStateFlow()
    val walkingTimeSeconds = _walkingTimeSeconds.asStateFlow()
    val isTracking = _isTracking.asStateFlow()
    val caloriesBurned = _caloriesBurned.asStateFlow()
    val locationUiState = _locationUiState.asStateFlow()
    val areaInSqMeters = _areaInSqMeters.asStateFlow()
    val score = _score.asStateFlow()
    val activity = _activity.asStateFlow()

    fun setTrackingState(isTracking: Boolean) {
        _isTracking.value = isTracking
    }

    fun updateMetrics(steps: Long, calories: Int) {
        _nrOfSteps.value = steps
        _caloriesBurned.value = calories
    }

    fun updateArea(area: Double) {
        _areaInSqMeters.value = area
    }

    fun tickTimer() {
        _walkingTimeSeconds.update { it + 1 }
    }

    fun calculateScore(): Long {
        return ScoreCalculator.CalculateScore(_nrOfSteps.value, _caloriesBurned.value.toLong(),
            _walkingTimeSeconds.value, _areaInSqMeters.value, _activity.value)
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

    fun setActivity(newActivity: String) {
        _activity.value = newActivity
    }

    fun reset() {
        _nrOfSteps.value = 0L
        _walkingTimeSeconds.value = 0L
        _caloriesBurned.value = 0
        _locationUiState.value = LocationUiState()
        _areaInSqMeters.value = 0.0
        _score.value = 0
        _activity.value = "Walking"
    }
}
