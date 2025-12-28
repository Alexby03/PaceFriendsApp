package com.kth.stepapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.core.services.CaloriesCalculator
import com.kth.stepapp.core.services.LocationService
import com.kth.stepapp.core.services.StepCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface DemoViewModel {

    val nrOfSteps: StateFlow<Long>

    val caloriesBurned: StateFlow<Int>

    val walkingTimeSeconds: StateFlow<Long>

    val locationUiState: StateFlow<LocationUiState>

    fun startTrackingLocation()

    fun startTimer()

    fun startTrackingStepsAndCals()

}

class DemoVM (
    private val stepCounter: StepCounter,
    private val locationService: LocationService,
    private val caloriesCalculator: CaloriesCalculator
): DemoViewModel, ViewModel() {

    private val _nrOfSteps = MutableStateFlow(0L)
    override val nrOfSteps: StateFlow<Long> = _nrOfSteps.asStateFlow()

    private val _caloriesBurned = MutableStateFlow(0)
    override val caloriesBurned: StateFlow<Int> = _caloriesBurned.asStateFlow()
    private val _walkingTimeSeconds = MutableStateFlow(0L)
    override val walkingTimeSeconds: StateFlow<Long> = _walkingTimeSeconds.asStateFlow()

    private val _locationUiState = MutableStateFlow(LocationUiState())
    override val locationUiState: StateFlow<LocationUiState> = _locationUiState.asStateFlow()

    override fun startTrackingLocation() {
        viewModelScope.launch {
            Log.d("LocationService", "ViewModel: startTracking() called. Subscribing to flow...")
            try {
                locationService.getLocationUpdates(intervalMs = 1000)
                    .catch { e ->
                        Log.e("LocationService", "ViewModel Flow Error: ${e.message}")
                    }
                    .collect { location ->
                        val newPoint = location.latitude to location.longitude

                        Log.d("LocationService", "ViewModel: Received ${location.latitude}, ${location.longitude}. Updating State.")

                        _locationUiState.update { oldState ->
                            val newCount = oldState.pathPoints.size + 1
                            if (newCount % 30 == 0) Log.v("LocationService", "ViewModel: Path now has $newCount points")

                            oldState.copy(
                                currentLat = location.latitude,
                                currentLng = location.longitude,
                                pathPoints = oldState.pathPoints + newPoint
                            )
                        }
                    }
            } catch (e: Exception) {
                Log.e("LocationService", "ViewModel: Fatal Exception in collection: ${e.message}")
            }
        }
    }

    override fun startTimer() {

    }

    override fun startTrackingStepsAndCals() {
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(1_000)
                _walkingTimeSeconds.value += 1
            }
        }
        viewModelScope.launch {
            Log.d("DemoVM", "Coroutine launched, waiting for sensor...")

            stepCounter.getStepCounts()
                .onStart { Log.d("DemoVM", "Flow started collecting") }
                .catch { e -> Log.e("DemoVM", "Flow error: ${e.message}") }
                .collect { steps ->
                    Log.d("DemoVM","NR OF STEPS $steps")
                    _nrOfSteps.value = steps
                    _caloriesBurned.value = caloriesCalculator.calculateCalories(
                        steps,
                        walkingTimeSeconds = _walkingTimeSeconds.value
                    )
                }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)

                DemoVM (
                    stepCounter = app.stepCounter,
                    locationService = app.locationService,
                    caloriesCalculator = CaloriesCalculator()
                )
            }
        }
    }

    init {

    }
}

data class LocationUiState(
    val currentLat: Double = 0.0,
    val currentLng: Double = 0.0,
    val pathPoints: List<Pair<Double, Double>> = emptyList()
)

class FakeDemoVM: DemoViewModel {
    override val nrOfSteps = MutableStateFlow(1L)

    override val caloriesBurned = MutableStateFlow(180)

    override val walkingTimeSeconds = MutableStateFlow(53L)

    override val locationUiState = MutableStateFlow(LocationUiState())

    override fun startTrackingLocation() { }

    override fun startTimer() { }

    override fun startTrackingStepsAndCals() { }
}