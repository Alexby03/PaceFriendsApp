package com.kth.stepapp.ui.viewmodels

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.core.entities.LocationUiState
import com.kth.stepapp.core.services.TrackingService
import com.kth.stepapp.data.repositories.TrackingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface DemoViewModel {
    val nrOfSteps: StateFlow<Long>
    val caloriesBurned: StateFlow<Int>
    val walkingTimeSeconds: StateFlow<Long>
    val locationUiState: StateFlow<LocationUiState>

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

    override fun startTracking() { }

    override fun stopTracking() { }
}