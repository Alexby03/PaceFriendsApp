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
import android.location.Location
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kth.stepapp.core.entities.DayEntryDto
import com.kth.stepapp.core.entities.RoutePointDto
import com.kth.stepapp.core.services.CaloriesCalculator
import com.kth.stepapp.core.utils.GeometryUtils
import com.kth.stepapp.data.repositories.PaceFriendsRepository
import com.kth.stepapp.data.repositories.PlayerRepository
import kotlinx.coroutines.launch
import java.time.Instant

interface ActivityViewModel {
    val nrOfSteps: StateFlow<Long>
    val caloriesBurned: StateFlow<Int>
    val walkingTimeSeconds: StateFlow<Long>
    val locationUiState: StateFlow<LocationUiState>
    val isTracking: StateFlow<Boolean>
    val areaInSqMeters: StateFlow<Double>
    val currentActivity: String
    fun startTracking(activityType: String)
    fun stopTracking(closedCircuit: Boolean)

    fun tryCompleteArea(): Boolean
}

class ActivityVM(
    private val paceFriendsRepository: PaceFriendsRepository,
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

    override fun startTracking(activityType: String) {
        TrackingRepository.setActivity(activityType)
        Intent(app, TrackingService::class.java).also {
            it.action = "START"
            it.putExtra("ACTIVITY_TYPE", activityType)
            app.startService(it)
        }
    }

    override fun stopTracking(closedCircuit: Boolean) {
        viewModelScope.launch {
            try {
                paceFriendsRepository.syncActivity(
                    PlayerRepository.playerId.value!!,
                    DayEntryDto(
                        date = Instant.now().toString(),
                        totalSteps = nrOfSteps.value,
                        totalCalories = caloriesBurned.value.toLong(),
                        timeSpentSeconds = walkingTimeSeconds.value,
                        areaInSquareMeters = if (closedCircuit) areaInSqMeters.value else 0.0,
                        score = TrackingRepository.calculateScore(),
                        activity = currentActivity,
                        routePoints = locationUiState.value.pathPoints.mapIndexed { index, pair ->
                            RoutePointDto(
                                latitude = pair.first,
                                longitude = pair.second,
                                sequenceOrder = index
                            )
                        }
                    )
                )
            } catch (e: Exception) {
                Log.e("Activity", "Failed to sync activity", e)
            }
            try {
                Intent(app, TrackingService::class.java).also {
                    it.action = "STOP"
                    app.startService(it)
                }
            } catch (e: Exception) {
                Log.e("Activity", "Failed to stop tracking service", e)
            }
        }
    }

    override fun tryCompleteArea(): Boolean {
        return GeometryUtils.calculateDistance(locationUiState.value.pathPoints) < 30f
    }

    init {
        TrackingRepository.reset()
    }

    companion object {
        fun provideFactory(
            app: Application,
            activityType: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ActivityVM(PaceFriendsRepository(), app, activityType) as T
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

    override fun startTracking(activityType: String) { }

    override fun stopTracking(closedCircuit: Boolean) { }

    override fun tryCompleteArea(): Boolean { return false }
}