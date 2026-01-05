package com.kth.stepapp.ui.viewmodels

import android.app.Application
import android.content.Intent
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

interface ActivityViewModel {
    val nrOfSteps: StateFlow<Long>
    val caloriesBurned: StateFlow<Int>
    val walkingTimeSeconds: StateFlow<Long>
    val locationUiState: StateFlow<LocationUiState>
    val isTracking: StateFlow<Boolean>
    val areaInSqMeters: StateFlow<Double>
    val currentActivity: String
    fun startTracking()
    fun stopTracking()
}

class ActivityVM(
    private val app: Application,
    activityType: String
) : ActivityViewModel, ViewModel() {

    override val nrOfSteps = TrackingRepository.nrOfSteps
    override val caloriesBurned = TrackingRepository.caloriesBurned
    override val walkingTimeSeconds = TrackingRepository.walkingTimeSeconds
    override val locationUiState = TrackingRepository.locationUiState
    override val isTracking = TrackingRepository.isTracking
    override val areaInSqMeters = TrackingRepository.areaInSqMeters
    override val currentActivity: String = activityType

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

    companion object {
        fun provideFactory(
            app: Application,
            activityType: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ActivityVM(app, activityType) as T
            }
        }
    }
}

class FakeActivityVM: ActivityViewModel {
    override val nrOfSteps = MutableStateFlow(1L)

    override val caloriesBurned = MutableStateFlow(180)

    override val walkingTimeSeconds = MutableStateFlow(53L)

    override val locationUiState = MutableStateFlow(LocationUiState())

    override val isTracking = MutableStateFlow(false)

    override val areaInSqMeters = MutableStateFlow(0.0)

    override val currentActivity = "Walking"

    override fun startTracking() { }

    override fun stopTracking() { }
}