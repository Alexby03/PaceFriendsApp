package com.kth.stepapp.ui.viewmodels

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.core.entities.LocationUiState
import com.kth.stepapp.core.services.TrackingService
import com.kth.stepapp.data.repositories.TrackingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface DemoViewModel {
    val nrOfSteps: StateFlow<Long>
    val caloriesBurned: StateFlow<Int>
    val walkingTimeSeconds: StateFlow<Long>
    val locationUiState: StateFlow<LocationUiState>
    val currentMapTile: StateFlow<Bitmap?>
    val isTracking: StateFlow<Boolean>
    fun startTracking()
    fun stopTracking()
}

class DemoVM(
    private val app: Application
) : DemoViewModel, ViewModel() {

    override val nrOfSteps = TrackingRepository.nrOfSteps
    override val caloriesBurned = TrackingRepository.caloriesBurned
    override val walkingTimeSeconds = TrackingRepository.walkingTimeSeconds
    override val locationUiState = TrackingRepository.locationUiState
    override val isTracking = TrackingRepository.isTracking

    private val _currentMapTile: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    override val currentMapTile = _currentMapTile.asStateFlow()

    override fun startTracking() {
        Intent(app, TrackingService::class.java).also {
            it.action = "START"
            app.startService(it)
        }
    }

    override fun stopTracking() {
        Intent(app, TrackingService::class.java).also {
            it.action = "STOP"
            app.startService(it)
        }
        //saveRun()
    }

//    fun saveRun() {
//        viewModelScope.launch {
//            // Take current values from the static Repository
//            val finalSteps = TrackingRepository.nrOfSteps.value
//            val finalCals = TrackingRepository.caloriesBurned.value
//
//            // Save to database
//            historyRepository.insertRun(date = now(), steps = finalSteps, calories = finalCals)
//        }
//    }
//
//    init {
//        viewModelScope.launch {
//            locationUiState.collect { state ->
//                Log.d("DemoVM", "Location update: ${state.currentLat}, ${state.currentLng}")
//                val lat = state.currentLat
//                val lon = state.currentLng
//                if (lat == 0.0 && lon == 0.0) return@collect
//
//                val zoom = 16
//
//                val bitmap = MapRepository.getMapTile(lat, lon, zoom)
//                Log.d("DemoVM", "Bitmap result: $bitmap")
//                if (bitmap != null) {
//                    _currentMapTile.value = bitmap
//                }
//            }
//        }
//    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as PaceFriendsApplication)
                DemoVM(app = app)
            }
        }
    }
}

class FakeDemoVM: DemoViewModel {
    override val nrOfSteps = MutableStateFlow(1L)

    override val caloriesBurned = MutableStateFlow(180)

    override val walkingTimeSeconds = MutableStateFlow(53L)

    override val locationUiState = MutableStateFlow(LocationUiState())

    override val isTracking = MutableStateFlow(false)

    override val currentMapTile = MutableStateFlow(null)

    override fun startTracking() { }

    override fun stopTracking() { }
}