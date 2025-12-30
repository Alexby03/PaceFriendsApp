package com.kth.stepapp.core.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.data.repositories.TrackingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.kth.stepapp.R
import com.kth.stepapp.core.utils.GeometryUtils

class TrackingService : Service() {
    private lateinit var locationService: LocationService
    private lateinit var stepCounter: StepCounter
    private val caloriesCalculator = CaloriesCalculator()
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        val app = applicationContext as PaceFriendsApplication
        locationService = app.locationService
        stepCounter = app.stepCounter

        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == "START") start()
        if (action == "STOP") stop()
        return START_STICKY
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "TRACKING_CHANNEL")
            .setContentTitle("Tracking your current session...")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
        startForeground(1, notification)

        // Initialize
        TrackingRepository.reset()
        TrackingRepository.setTrackingState(true)

        // Timer
        serviceScope.launch {
            while (true) {
                kotlinx.coroutines.delay(1000L)
                TrackingRepository.tickTimer()
            }
        }

        // Steps and Cals
        serviceScope.launch {
            stepCounter.getStepCounts().collect { steps ->
                val currentSeconds = TrackingRepository.walkingTimeSeconds.value
                val calories = caloriesCalculator.calculateCalories(
                    steps = steps,
                    walkingTimeSeconds = currentSeconds
                )
                TrackingRepository.updateMetrics(steps, calories)
            }
        }

        // GPS
        serviceScope.launch {
            locationService.getLocationUpdates(1000).collect { location ->
                TrackingRepository.addLocationPoint(location.latitude, location.longitude)
                GeometryUtils.calculateArea(TrackingRepository.locationUiState.value.pathPoints)
            }
        }
    }

    private fun stop() {
        TrackingRepository.setTrackingState(false)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        serviceScope.cancel()
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                "TRACKING_CHANNEL",
                "Workout Tracking",
                android.app.NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(android.app.NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}